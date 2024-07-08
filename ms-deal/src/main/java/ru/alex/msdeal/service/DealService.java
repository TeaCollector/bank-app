package ru.alex.msdeal.service;

import java.time.Instant;
import java.util.*;
import javax.transaction.Transactional;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.alex.msdeal.config.EmailSender;
import ru.alex.msdeal.dto.*;
import ru.alex.msdeal.entity.*;
import ru.alex.msdeal.entity.constant.ChangeType;
import ru.alex.msdeal.entity.constant.CreditStatus;
import ru.alex.msdeal.entity.constant.StatementStatus;
import ru.alex.msdeal.entity.constant.Theme;
import ru.alex.msdeal.exception.ClientDeniedException;
import ru.alex.msdeal.exception.SesCodeNotEqualsException;
import ru.alex.msdeal.exception.StatementNotFoundException;
import ru.alex.msdeal.exception.StatementNotPreApprovedException;
import ru.alex.msdeal.mapper.ClientMapper;
import ru.alex.msdeal.mapper.CreditMapper;
import ru.alex.msdeal.mapper.EmploymentMapper;
import ru.alex.msdeal.mapper.PassportMapper;
import ru.alex.msdeal.repository.ClientRepository;
import ru.alex.msdeal.repository.CreditRepository;
import ru.alex.msdeal.repository.StatementRepository;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DealService {

    private final CalculatorFeignClient calculatorFeignClient;

    private final ClientRepository clientRepository;
    private final StatementRepository statementRepository;
    private final CreditRepository creditRepository;

    private final EmploymentMapper employmentMapper;
    private final ClientMapper clientMapper;
    private final PassportMapper passportMapper;
    private final CreditMapper creditMapper;

    private final EmailSender emailSender;

    public List<LoanOfferDto> offer(LoanStatementRequestDto loanStatementRequestDto) {
        log.info("Client try to take offer {}", loanStatementRequestDto);

        var clientEntity = createClientEntity(loanStatementRequestDto);
        var savedClient = clientRepository.save(clientEntity);
        log.info("Client {} {} was saved successful", savedClient.getFirstName(), clientEntity.getLastName());

        var loanOfferDtos = calculatorFeignClient.sendLoanOffer(loanStatementRequestDto);
        var statement = createStatementEntity(savedClient);

        loanOfferDtos.forEach(loanOfferDto -> loanOfferDto.setStatementId(statement.getId()));
        log.info("Offer list {} was calculated successful", loanOfferDtos);

        return loanOfferDtos;
    }

    public void selectOffer(LoanOfferDto loanOfferDto) {
        var statement = statementRepository.findById(loanOfferDto.getStatementId())
            .orElseThrow(() -> new StatementNotFoundException("Statement №" + loanOfferDto.getStatementId() + " not found"));

        if (Objects.isNull(loanOfferDto.getStatementId())) {
            updateStatusAndHistory(statement, StatementStatus.CLIENT_DENIED, "Client denied to take loan");
            throw new ClientDeniedException("You denied loan offer");
        }

        log.info("Offer {} was selected by client {} {}", loanOfferDto.getStatementId(),
            statement.getClient().getLastName(),
            statement.getClient().getFirstName());

        saveSelectedOffer(loanOfferDto, statement);
        log.info("Statement attribute was successful updated");

        updateStatusAndHistory(statement, StatementStatus.PREAPPROVAL, "Pre approval loan");
        emailSender.sendMail(buildMessage(statement.getClient(), statement, Theme.FINISH_REGISTRATION));
    }

    public void calculate(FinishRegistrationRequestDto requestDto, String statementId) {
        log.info("By statementId {} calculating credit offer with data {}", statementId, requestDto);
        var statement = getStatement(statementId);

        if (statement.getStatus() != StatementStatus.PREAPPROVAL) {
            throw new StatementNotPreApprovedException("You not pre approved your statement");
        }

        var client = statement.getClient();
        var passport = updatePassportData(requestDto, client);

        updateEmployment(requestDto, client, passport);

        var scoringDataDto = createScoringDto(requestDto, passport, statement, client);

        var creditDto = new CreditDto();
        try {
            creditDto = calculatorFeignClient.calculateCreditOffer(scoringDataDto);
        } catch (FeignException.FeignClientException exception) {
            updateStatusAndHistory(statement, StatementStatus.CC_DENIED, "Loan was denied");
            emailSender.sendMail(buildMessage(client, statement, Theme.STATEMENT_DENIED));
            throw exception;
        }

        log.info("Credit was successful calculated {}", creditDto);

        var creditEntity = creditMapper.toEntity(creditDto);
        creditEntity.setCreditStatus(CreditStatus.CALCULATED);

        statement.setCredit(creditEntity);
        statement.setSignDate(Instant.now());

        updateStatusAndHistory(statement, StatementStatus.CC_APPROVED, "Credit was calculated, all fine");

        log.info("Credit was successful calculated all variables was updated");

        emailSender.sendMail(buildMessage(client, statement, Theme.CREATE_DOCUMENT));
    }

    public void sendDocument(String statementId) {
        var statement = getStatement(statementId);

        updateStatusAndHistory(statement, StatementStatus.PREPARE_DOCUMENTS, "Preparing documents");

        emailSender.sendMail(buildMessage(statement.getClient(), statement, Theme.SEND_DOCUMENT));
        log.info("Client: {} receive document", statement.getClient().getEmail());
    }

    public void signDocument(String statementId) {
        var statement = getStatement(statementId);

        var sesCode = createRandomSesCode();
        statement.setSesCode(sesCode);

        updateStatusAndHistory(statement, StatementStatus.DOCUMENT_CREATED, "Document was created");

        emailSender.sendMail(buildMessage(statement.getClient(), statement, Theme.SEND_SES, sesCode));
        log.info("Ses-code was sent to client: {}", statement.getClient().getEmail());
    }

    private int createRandomSesCode() {
        var number = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            number.append(new Random().nextInt(0, 9));
        }

        return Integer.parseInt(number.toString());
    }


    public void signCode(SesCodeDto sesCodeDto, String statementId) {
        var statement = getStatement(statementId);

        if (statement.getSesCode().equals(sesCodeDto.getSesCode())) {
            emailSender.sendMail(buildMessage(statement.getClient(), statement, Theme.CREDIT_ISSUED));
        } else {
            throw new SesCodeNotEqualsException("Sorry ses-code is not equals");
        }

        var credit = statement.getCredit();
        credit.setCreditStatus(CreditStatus.ISSUED);

        updateStatusAndHistory(statement, StatementStatus.DOCUMENT_SIGNED, "Document was signed");
        updateStatusAndHistory(statement, StatementStatus.CREDIT_ISSUED, "Loan was taken, all fine");
    }


    private Statement getStatement(String statementId) {
        return statementRepository.findById(UUID.fromString(statementId))
            .orElseThrow(() -> new StatementNotFoundException("Statement №" + statementId + " not found"));
    }

    private Passport updatePassportData(FinishRegistrationRequestDto requestDto, Client client) {
        return client.getPassport()
            .toBuilder()
            .issueDate(requestDto.getPassportIssueDate())
            .issueBranch(requestDto.getPassportIssueBranch())
            .build();
    }

    private Client createClientEntity(LoanStatementRequestDto loanStatementRequestDto) {
        var clientEntity = clientMapper.toEntity(loanStatementRequestDto);
        var passport = passportMapper.toEntity(loanStatementRequestDto);
        passport.setId(UUID.randomUUID());
        clientEntity.setPassport(passport);
        return clientEntity;
    }

    private Statement createStatementEntity(Client savedClient) {
        var statement = Statement.builder()
            .statusHistory(new ArrayList<>())
            .client(savedClient)
            .creationDate(Instant.now())
            .build();

        var statementEntity = statementRepository.save(statement);
        statementEntity.getStatusHistory().add(StatusHistory.builder()
            .changeType(ChangeType.AUTOMATIC)
            .status("List with loan offer was issued")
            .time(Instant.now())
            .build());

        return statementEntity;
    }

    private EmailMessage buildMessage(Client client, Statement statement, Theme theme, int sesCode) {
        return EmailMessage.builder()
            .address(client.getEmail())
            .theme(theme)
            .statementId(statement.getId())
            .sesCode(sesCode)
            .build();
    }

    private EmailMessage buildMessage(Client client, Statement statement, Theme theme) {
        return EmailMessage.builder()
            .address(client.getEmail())
            .theme(theme)
            .statementId(statement.getId())
            .build();
    }

    private void updateEmployment(FinishRegistrationRequestDto requestDto, Client client, Passport passport) {
        var employment = employmentMapper.toEntity(requestDto.getEmploymentDto());
        employment.setId(UUID.randomUUID());

        client.setGender(requestDto.getGender());
        client.setAccountNumber(requestDto.getAccountNumber());
        client.setMaritalStatus(requestDto.getMaritalStatus());
        client.setDependentAmount(requestDto.getDependentAmount());
        client.setEmployment(employment);
        client.setPassport(passport);
    }

    private void updateStatusAndHistory(Statement statement, StatementStatus status, String statusInfo) {
        statement.setStatus(status);
        statement.getStatusHistory().add(StatusHistory.builder()
            .changeType(ChangeType.AUTOMATIC)
            .status(statusInfo)
            .time(Instant.now())
            .build());
    }

    private void saveSelectedOffer(LoanOfferDto loanOfferDto, Statement statement) {
        statement.setAppliedOffer(AppliedOffer.builder()
            .statementId(loanOfferDto.getStatementId())
            .rate(loanOfferDto.getRate())
            .term(loanOfferDto.getTerm())
            .isInsuranceEnabled(loanOfferDto.getIsInsuranceEnabled())
            .isSalaryClient(loanOfferDto.getIsSalaryClient())
            .monthlyPayment(loanOfferDto.getMonthlyPayment())
            .requestedAmount(loanOfferDto.getRequestAmount())
            .totalAmount(loanOfferDto.getTotalAmount())
            .build());
    }

    private ScoringDataDto createScoringDto(FinishRegistrationRequestDto requestDto, Passport passport, Statement statement, Client client) {
        return ScoringDataDto.builder()
            .gender(requestDto.getGender())
            .maritalStatus(requestDto.getMaritalStatus())
            .dependentAmount(requestDto.getDependentAmount())
            .passportIssueDate(requestDto.getPassportIssueDate())
            .passportIssueBranch(requestDto.getPassportIssueBranch())
            .account(requestDto.getAccountNumber())
            .employment(requestDto.getEmploymentDto())
            .passportSeries(passport.getSeries())
            .passportNumber(passport.getNumber())
            .term(statement.getAppliedOffer().term())
            .amount(statement.getAppliedOffer().requestedAmount())
            .isInsuranceEnabled(statement.getAppliedOffer().isInsuranceEnabled())
            .isSalaryClient(statement.getAppliedOffer().isSalaryClient())
            .firstName(client.getFirstName())
            .lastName(client.getLastName())
            .middleName(client.getMiddleName())
            .birthdate(client.getBirthdate())
            .build();
    }
}

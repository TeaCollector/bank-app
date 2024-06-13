package ru.alex.msdeal.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.alex.msdeal.dto.FinishRegistrationRequestDto;
import ru.alex.msdeal.dto.LoanOfferDto;
import ru.alex.msdeal.dto.LoanStatementRequestDto;
import ru.alex.msdeal.dto.ScoringDataDto;
import ru.alex.msdeal.entity.*;
import ru.alex.msdeal.entity.constant.ChangeType;
import ru.alex.msdeal.entity.constant.CreditStatus;
import ru.alex.msdeal.entity.constant.StatementStatus;
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
        log.info("Offer {} was selected by client {} {}", loanOfferDto.getStatementId(),
            statement.getClient().getLastName(),
            statement.getClient().getFirstName());

        changeStatusAndHistory(statement, StatementStatus.PREAPPROVAL, "Клиент выбрал оффер");
        saveSelectedOffer(loanOfferDto, statement);
        log.info("Statement attribute was successful updated");
    }

    public void calculate(FinishRegistrationRequestDto requestDto, String statementId) {
        log.info("By statementId {} calculating credit offer with data {}", statementId, requestDto);
        var statement = statementRepository.findById(UUID.fromString(statementId))
            .orElseThrow(() -> new StatementNotFoundException("Statement №" + statementId + " not found"));

        if (statement.getStatus() != StatementStatus.PREAPPROVAL) {
            throw new StatementNotPreApprovedException("you not pre approved your statement");
        }

        var client = statement.getClient();
        var passport = updatePassportData(requestDto, client);

        updateEmployment(requestDto, client, passport);

        var scoringDataDto = createScoringDto(requestDto, passport, statement, client);

        var creditDto = calculatorFeignClient.calculateCreditOffer(scoringDataDto);
        log.info("Credit was calculated {}", creditDto);

        var creditEntity = creditMapper.toEntity(creditDto);
        creditEntity.setCreditStatus(CreditStatus.CALCULATED);

        creditRepository.save(creditEntity);
        statement.setCredit(creditEntity);
        statement.setSignDate(Instant.now());

        changeStatusAndHistory(statement, StatementStatus.APPROVED, "Кредит высчитан, всё хорошо");

        log.info("Credit was successful calculated all variables was updated");
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
            .sesCode(100)
            .creationDate(Instant.now())
            .build();

        var statementEntity = statementRepository.save(statement);
        statementEntity.getStatusHistory().add(StatusHistory.builder()
            .changeType(ChangeType.AUTOMATIC)
            .status("Выдан список с предварительными условиями кредита")
            .time(Instant.now())
            .build());

        return statementEntity;
    }

    private void changeStatusAndHistory(Statement statement, StatementStatus status, String statusInfo) {
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

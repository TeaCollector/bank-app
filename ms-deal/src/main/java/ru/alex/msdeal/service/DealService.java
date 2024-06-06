package ru.alex.msdeal.service;

import java.time.Instant;
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
import ru.alex.msdeal.entity.AppliedOffer;
import ru.alex.msdeal.entity.Statement;
import ru.alex.msdeal.entity.StatusHistory;
import ru.alex.msdeal.entity.constant.ChangeType;
import ru.alex.msdeal.entity.constant.StatementStatus;
import ru.alex.msdeal.mapper.ClientMapper;
import ru.alex.msdeal.mapper.PassportMapper;
import ru.alex.msdeal.repository.ClientRepository;
import ru.alex.msdeal.repository.StatementRepository;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DealService {

    private final CalculatorFeignClient calculatorFeignClient;
    private final ClientRepository clientRepository;
    private final StatementRepository statementRepository;
    private final ClientMapper clientMapper;
    private final PassportMapper passportMapper;

    public List<LoanOfferDto> offer(LoanStatementRequestDto loanStatementRequestDto) {
        var clientEntity = clientMapper.toEntity(loanStatementRequestDto);
        var passport = passportMapper.toEntity(loanStatementRequestDto);
        clientEntity.setPassport(passport);

        var savedClient = clientRepository.save(clientEntity);

        List<LoanOfferDto> loanOfferDtos = calculatorFeignClient.calculateLoanOffer(loanStatementRequestDto);

        log.info("Result: {}", loanOfferDtos);


        var statement = Statement.builder()
            .client(savedClient)
            .sesCode(100)
            .creationDate(Instant.now())
            .status(StatementStatus.PREAPPROVAL)
            .build();

        var statementId = statementRepository.save(statement).getId();
        loanOfferDtos.forEach(loanOfferDto -> loanOfferDto.setStatementId(statementId));

        return loanOfferDtos;
    }

    public void selectOffer(LoanOfferDto loanOfferDto) {
        var statement = statementRepository.getReferenceById(loanOfferDto.getStatementId());

        statement.setStatus(StatementStatus.APPROVED);
        statement.setStatusHistory(StatusHistory.builder()
            .changeType(ChangeType.MANUAL)
            .status("Client select offer")
            .time(Instant.now())
            .build());
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

    public void calculate(FinishRegistrationRequestDto finishRegistrationRequestDto, String statementId) {
        var statement = statementRepository.getReferenceById(UUID.fromString(statementId));
        var client = statement.getClient();
        var passport = client.getPassport();
        var scoringDataDto = ScoringDataDto.builder()
            .gender(finishRegistrationRequestDto.getGender())
            .maritalStatus(finishRegistrationRequestDto.getMaritalStatus())
            .dependentAmount(finishRegistrationRequestDto.getDependentAmount())
            .passportIssueDate(client.getPassport().getIssueDate())
            .passportIssueBranch(client.getPassport().getIssueBranch())
            .account(finishRegistrationRequestDto.getAccountNumber())
            .employment(finishRegistrationRequestDto.getEmploymentDto())
            .term(statement.getAppliedOffer().term())
            .amount(statement.getAppliedOffer().requestedAmount())
            .firstName(client.getFirstName())
            .lastName(client.getLastName())
            .middleName(client.getMiddleName())
            .birthdate(client.getBirthdate())
            .passportSeries(passport.getSeries())
            .passportNumber(passport.getNumber())
            .isInsuranceEnabled(statement.getAppliedOffer().isInsuranceEnabled())
            .isSalaryClient(statement.getAppliedOffer().isSalaryClient())
            .build();

        calculatorFeignClient.calculateCreditOffer(scoringDataDto);
        log.info("SUCCESS!!!");
//
//
//        client.setEmployment(finishRegistrationRequestDto.getEmploymentDto());


    }
}

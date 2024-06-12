package ru.alex.msdeal.util;

import lombok.experimental.UtilityClass;
import ru.alex.msdeal.dto.CreditDto;
import ru.alex.msdeal.dto.LoanOfferDto;
import ru.alex.msdeal.dto.LoanStatementRequestDto;
import ru.alex.msdeal.dto.PaymentScheduleElementDto;
import ru.alex.msdeal.entity.Client;
import ru.alex.msdeal.entity.Credit;
import ru.alex.msdeal.entity.Employment;
import ru.alex.msdeal.entity.Passport;
import ru.alex.msdeal.entity.constant.*;
import ru.alex.msdeal.mapper.PaymentScheduleMapper;
import ru.alex.msdeal.mapper.PaymentScheduleMapperImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@UtilityClass
public class DataForTest {

    PaymentScheduleMapper paymentScheduleMapper = new PaymentScheduleMapperImpl();

    public UUID getStatementId() {
        return UUID.fromString("80b2d767-b937-49f7-a215-185f45fa3e47");
    }

    public UUID getCreditId() {
        return UUID.fromString("360b28a7-c2c8-45f2-a197-404d38dc329a");
    }

    public UUID getClientId() {
        return UUID.fromString("7e68f4d6-0311-4a04-8ab5-e0b2082dd7ea");
    }

    public Credit getCreditEntity() {
        return Credit.builder()
                .creditStatus(CreditStatus.CALCULATED)
                .salaryClient(false)
                .insuranceEnabled(false)
                .statement(null)
                .term(6)
                .paymentSchedule(paymentScheduleMapper.toEntityList(getPaymentSchedule()))
                .rate(BigDecimal.valueOf(14))
                .psk(BigDecimal.valueOf(312_368))
                .monthlyPayment(BigDecimal.valueOf(52_061.4))
                .build();
    }

    public Client getClientEntity() {
        return Client.builder()
            .email("petya@gmail.com")
            .birthdate(LocalDate.now())
            .lastName("Petrov")
            .firstName("Petya")
            .middleName("Petrovich")
            .employment(Employment.builder()
                .employerInn("456243688462")
                .employmentsPosition(EmploymentPosition.TOP_MANAGER)
                .salary(BigDecimal.valueOf(350_000))
                .workExperienceCurrent(9)
                .workExperienceTotal(60)
                .employmentStatus(EmploymentStatus.EMPLOYED)
                .build())
            .dependentAmount(30_000)
            .passport(Passport.builder()
                .series("5531")
                .number("445235")
                .issueDate(LocalDate.of(2010, 5, 15))
                .issueBranch("OUFMS Russia")
                .build())
            .gender(Gender.MALE)
            .maritalStatus(MaritalStatus.MARRIED)
            .statement(null)
            .accountNumber("459230050234")
            .build();
    }

    public String getLoanStatementRequestBody() {
        return """
            {
              "amount": 300000.00,
              "term": 6,
              "firstName": "Alexandr",
              "lastName": "Sergeev",
              "middleName": "Yurievich",
              "email": "sasha@gmail.com",
              "birthdate": "1992-05-21",
              "passportSeries": "4456",
              "passportNumber": "346894"
            }
            """;
    }

    public LoanStatementRequestDto getLoanStatementObject() {
        return LoanStatementRequestDto.builder()
            .email("sasha@gmail.com")
            .amount(BigDecimal.valueOf(300_000.00))
            .passportSeries("4456")
            .passportNumber("346894")
            .birthdate(LocalDate.of(1992, 5, 21))
            .firstName("Alexandr")
            .lastName("Sergeev")
            .middleName("Yurievich")
            .term(6)
            .build();
    }


    public List<LoanOfferDto> getLoanOfferDto() {
        var loanOfferDto1 = LoanOfferDto.builder()
            .rate(BigDecimal.valueOf(15.00))
            .requestAmount(BigDecimal.valueOf(300_000.00))
            .totalAmount(BigDecimal.valueOf(300_000.00))
            .monthlyPayment(BigDecimal.valueOf(52_210.14))
            .term(6)
            .isInsuranceEnabled(false)
            .isSalaryClient(false)
            .build();

        var loanOfferDto2 = LoanOfferDto.builder()
            .rate(BigDecimal.valueOf(13.00))
            .requestAmount(BigDecimal.valueOf(300_000.00))
            .totalAmount(BigDecimal.valueOf(300_000.00))
            .monthlyPayment(BigDecimal.valueOf(51_912.87))
            .term(6)
            .isInsuranceEnabled(false)
            .isSalaryClient(true)
            .build();

        var loanOfferDto3 = LoanOfferDto.builder()
            .rate(BigDecimal.valueOf(12.00))
            .requestAmount(BigDecimal.valueOf(300_000.00))
            .totalAmount(BigDecimal.valueOf(306_750.00))
            .monthlyPayment(BigDecimal.valueOf(52929.22))
            .term(6)
            .isInsuranceEnabled(true)
            .isSalaryClient(false)
            .build();

        var loanOfferDto4 = LoanOfferDto.builder()
            .rate(BigDecimal.valueOf(10.00))
            .requestAmount(BigDecimal.valueOf(300_000.00))
            .totalAmount(BigDecimal.valueOf(306_750.00))
            .monthlyPayment(BigDecimal.valueOf(52_626.46))
            .term(6)
            .isInsuranceEnabled(true)
            .isSalaryClient(true)
            .build();

        return List.of(loanOfferDto1, loanOfferDto2, loanOfferDto3, loanOfferDto4);
    }

    public LoanOfferDto getSelectedLoanOffer() {
        return LoanOfferDto.builder()
            .requestAmount(BigDecimal.valueOf(300_000.00))
            .totalAmount(BigDecimal.valueOf(300_000.00))
            .monthlyPayment(BigDecimal.valueOf(52_210.14))
            .rate(BigDecimal.valueOf(15.00))
            .term(6)
            .isInsuranceEnabled(false)
            .isSalaryClient(false)
            .build();
    }

    public String getFinishRegistrationRequestDto() {
        return """
            {
              "gender": "MALE",
              "maritalStatus": "MARRIED",
              "dependentAmount": 25000,
              "passportIssueDate": "2010-05-04",
              "passportIssueBranch": "OUFMS Russia",
              "employmentDto":  {
                 "employmentStatus": "EMPLOYED",
                 "employerInn": "31565562234",
                 "salary": 90000,
                 "position": "TOP_MANAGER",
                 "workExperienceTotal": 50,
                 "workExperienceCurrent": 27
                },
              "accountNumber": "423556783345"
            }
            """;
    }

    public CreditDto getCreditDto() {
        return CreditDto.builder()
                .psk(BigDecimal.valueOf(312_368.40))
                .rate(BigDecimal.valueOf(14.00))
                .term(6)
                .amount(BigDecimal.valueOf(300_000L))
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .paymentSchedule(getPaymentSchedule())
                .build();
    }

    public List<PaymentScheduleElementDto> getPaymentSchedule() {
        return List.of(
                PaymentScheduleElementDto.builder()
                        .number(1)
                        .date(LocalDate.of(2024, 7, 11))
                        .totalAmount(BigDecimal.valueOf(300_000L))
                        .interestPayment(BigDecimal.valueOf(3_500.00))
                        .debtPayment(BigDecimal.valueOf(48_561.40))
                        .remainingDebt(BigDecimal.valueOf(251_438.60))
                        .build(),

                PaymentScheduleElementDto.builder()
                        .number(2)
                        .date(LocalDate.of(2024, 8, 11))
                        .totalAmount(BigDecimal.valueOf(300_000L))
                        .interestPayment(BigDecimal.valueOf(2_933.45))
                        .debtPayment(BigDecimal.valueOf(49_127.95))
                        .remainingDebt(BigDecimal.valueOf(202_310.65))
                        .build(),

                PaymentScheduleElementDto.builder()
                        .number(3)
                        .date(LocalDate.of(2024, 9, 11))
                        .totalAmount(BigDecimal.valueOf(300_000L))
                        .interestPayment(BigDecimal.valueOf(2_360.29))
                        .debtPayment(BigDecimal.valueOf(49_701.11))
                        .remainingDebt(BigDecimal.valueOf(152_609.54))
                        .build(),

                PaymentScheduleElementDto.builder()
                        .number(4)
                        .date(LocalDate.of(2024, 10, 11))
                        .totalAmount(BigDecimal.valueOf(300_000L))
                        .interestPayment(BigDecimal.valueOf(1_780.44))
                        .debtPayment(BigDecimal.valueOf(50_280.96))
                        .remainingDebt(BigDecimal.valueOf(102_328.58))
                        .build(),

                PaymentScheduleElementDto.builder()
                        .number(5)
                        .date(LocalDate.of(2024, 11, 11))
                        .totalAmount(BigDecimal.valueOf(300_000L))
                        .interestPayment(BigDecimal.valueOf(1_193.83))
                        .debtPayment(BigDecimal.valueOf(50_867.57))
                        .remainingDebt(BigDecimal.valueOf(51_461.01))
                        .build(),

                PaymentScheduleElementDto.builder()
                        .number(6)
                        .date(LocalDate.of(2024, 12, 11))
                        .totalAmount(BigDecimal.valueOf(300_000L))
                        .interestPayment(BigDecimal.valueOf(600.38))
                        .debtPayment(BigDecimal.valueOf(51_461.02))
                        .remainingDebt(BigDecimal.valueOf(-0.01))
                        .build()
        );
    }
}

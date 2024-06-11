package ru.alex.msdeal.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.experimental.UtilityClass;
import ru.alex.msdeal.dto.CreditDto;
import ru.alex.msdeal.dto.LoanOfferDto;
import ru.alex.msdeal.dto.LoanStatementRequestDto;
import ru.alex.msdeal.dto.PaymentScheduleElementDto;
import ru.alex.msdeal.entity.Client;
import ru.alex.msdeal.entity.Employment;
import ru.alex.msdeal.entity.Passport;
import ru.alex.msdeal.entity.constant.EmploymentPosition;
import ru.alex.msdeal.entity.constant.EmploymentStatus;
import ru.alex.msdeal.entity.constant.Gender;
import ru.alex.msdeal.entity.constant.MaritalStatus;


@UtilityClass
public class DataForTest {

    public Client getClientEntity() {
        return Client.builder()
            .email("sasha@gmail.com")
            .birthdate(LocalDate.now())
            .lastName("Sergeev")
            .firstName("Alexandr")
            .middleName("Yurievich")
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
            .amount(BigDecimal.valueOf(300000.00))
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
            .requestAmount(BigDecimal.valueOf(300000.00))
            .totalAmount(BigDecimal.valueOf(300000.00))
            .monthlyPayment(BigDecimal.valueOf(52210.14))
            .term(6)
            .isInsuranceEnabled(false)
            .isSalaryClient(false)
            .build();

        var loanOfferDto2 = LoanOfferDto.builder()
            .rate(BigDecimal.valueOf(13.00))
            .requestAmount(BigDecimal.valueOf(300000.00))
            .totalAmount(BigDecimal.valueOf(300000.00))
            .monthlyPayment(BigDecimal.valueOf(51912.87))
            .term(6)
            .isInsuranceEnabled(false)
            .isSalaryClient(true)
            .build();

        var loanOfferDto3 = LoanOfferDto.builder()
            .rate(BigDecimal.valueOf(12.00))
            .requestAmount(BigDecimal.valueOf(300000.00))
            .totalAmount(BigDecimal.valueOf(306750.00))
            .monthlyPayment(BigDecimal.valueOf(52929.22))
            .term(6)
            .isInsuranceEnabled(true)
            .isSalaryClient(false)
            .build();

        var loanOfferDto4 = LoanOfferDto.builder()
            .rate(BigDecimal.valueOf(10.00))
            .requestAmount(BigDecimal.valueOf(300000.00))
            .totalAmount(BigDecimal.valueOf(306750.00))
            .monthlyPayment(BigDecimal.valueOf(52626.46))
            .term(6)
            .isInsuranceEnabled(true)
            .isSalaryClient(true)
            .build();

        return List.of(loanOfferDto1, loanOfferDto2, loanOfferDto3, loanOfferDto4);
    }

    public LoanOfferDto getSelectedLoanOffer() {
        return LoanOfferDto.builder()
            .requestAmount(BigDecimal.valueOf(300000.00))
            .totalAmount(BigDecimal.valueOf(300000.00))
            .monthlyPayment(BigDecimal.valueOf(52210.14))
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
            .paymentSchedule(List.of(
                PaymentScheduleElementDto.builder()
                    .number(1)
                    .date(LocalDate.of(2024, 7, 11))
                    .totalAmount(BigDecimal.valueOf(300_000L))
                    .interestPayment(BigDecimal.valueOf(3500.00))
                    .debtPayment(BigDecimal.valueOf(48561.40))
                    .remainingDebt(BigDecimal.valueOf(251438.60))
                    .build(),

                PaymentScheduleElementDto.builder()
                    .number(2)
                    .date(LocalDate.of(2024, 8, 11))
                    .totalAmount(BigDecimal.valueOf(300_000L))
                    .interestPayment(BigDecimal.valueOf(2933.45))
                    .debtPayment(BigDecimal.valueOf(49127.95))
                    .remainingDebt(BigDecimal.valueOf(202310.65))
                    .build(),

                PaymentScheduleElementDto.builder()
                    .number(3)
                    .date(LocalDate.of(2024, 9, 11))
                    .totalAmount(BigDecimal.valueOf(300_000L))
                    .interestPayment(BigDecimal.valueOf(2360.29))
                    .debtPayment(BigDecimal.valueOf(49701.11))
                    .remainingDebt(BigDecimal.valueOf(152609.54))
                    .build(),

                PaymentScheduleElementDto.builder()
                    .number(4)
                    .date(LocalDate.of(2024, 10, 11))
                    .totalAmount(BigDecimal.valueOf(300_000L))
                    .interestPayment(BigDecimal.valueOf(1780.44))
                    .debtPayment(BigDecimal.valueOf(50280.96))
                    .remainingDebt(BigDecimal.valueOf(102328.58))
                    .build(),

                PaymentScheduleElementDto.builder()
                    .number(5)
                    .date(LocalDate.of(2024, 11, 11))
                    .totalAmount(BigDecimal.valueOf(300_000L))
                    .interestPayment(BigDecimal.valueOf(1193.83))
                    .debtPayment(BigDecimal.valueOf(50867.57))
                    .remainingDebt(BigDecimal.valueOf(51461.01))
                    .build(),

                PaymentScheduleElementDto.builder()
                    .number(6)
                    .date(LocalDate.of(2024, 12, 11))
                    .totalAmount(BigDecimal.valueOf(300_000L))
                    .interestPayment(BigDecimal.valueOf(600.38))
                    .debtPayment(BigDecimal.valueOf(51461.02))
                    .remainingDebt(BigDecimal.valueOf(-0.01))
                    .build()
            ))
            .build();
    }
}

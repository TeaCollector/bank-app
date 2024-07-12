package ru.alex.msstatement.util;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.experimental.UtilityClass;
import ru.alex.msstatement.dto.LoanOfferDto;


@UtilityClass
public class DataForTest {
    public List<LoanOfferDto> getLoanOfferDtos() {
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
            .statementId(UUID.randomUUID())
            .requestAmount(BigDecimal.valueOf(300_000.00))
            .totalAmount(BigDecimal.valueOf(300_000.00))
            .monthlyPayment(BigDecimal.valueOf(52_210.14))
            .rate(BigDecimal.valueOf(15.00))
            .term(6)
            .isInsuranceEnabled(false)
            .isSalaryClient(false)
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
}

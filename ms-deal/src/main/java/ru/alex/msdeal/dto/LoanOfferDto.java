package ru.alex.msdeal.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class LoanOfferDto {

    private UUID statementId;
    private BigDecimal requestAmount;
    private BigDecimal totalAmount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;

}

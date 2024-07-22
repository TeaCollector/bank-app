package ru.alex.msapigateway.dto;


import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;


@Builder
public record AppliedOffer(UUID statementId, BigDecimal requestedAmount,
                           BigDecimal totalAmount,
                           Integer term, BigDecimal monthlyPayment,
                           BigDecimal rate, Boolean isInsuranceEnabled,
                           Boolean isSalaryClient) {

}

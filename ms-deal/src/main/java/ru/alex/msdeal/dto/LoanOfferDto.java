package ru.alex.msdeal.dto;

import java.math.BigDecimal;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class LoanOfferDto {

    @NotNull
    private UUID statementId;
    private BigDecimal requestAmount;
    private BigDecimal totalAmount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;

}

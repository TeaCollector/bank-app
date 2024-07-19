package ru.alex.msdeal.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CreditDto {

    private BigDecimal amount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private BigDecimal psk;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
    private List<PaymentScheduleElementDto> paymentSchedule;
}

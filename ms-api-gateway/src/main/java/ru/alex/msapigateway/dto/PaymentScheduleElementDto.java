package ru.alex.msapigateway.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class PaymentScheduleElementDto {

    private Integer number;
    private LocalDate date;
    private BigDecimal totalAmount;
    private BigDecimal interestPayment;
    private BigDecimal debtPayment;
    private BigDecimal remainingDebt;
}

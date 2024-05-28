package ru.alex.mscalc.web.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PaymentScheduleElementDto {

    private Integer number;
    private LocalDate date;
    private BigDecimal totalAmount;
    private BigDecimal interestPayment;
    private BigDecimal debtPayment;
    private BigDecimal remainingDebt;
}

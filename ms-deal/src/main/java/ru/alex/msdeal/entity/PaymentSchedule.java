package ru.alex.msdeal.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class PaymentSchedule implements Serializable {
    private Integer number;
    private LocalDate date;
    private BigDecimal totalAmount;
    private BigDecimal interestPayment;
    private BigDecimal debtPayment;
    private BigDecimal remainingDebt;
}

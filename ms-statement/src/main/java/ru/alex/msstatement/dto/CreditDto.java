package ru.alex.msstatement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.alex.msstatement.dto.PaymentScheduleElementDto;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
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

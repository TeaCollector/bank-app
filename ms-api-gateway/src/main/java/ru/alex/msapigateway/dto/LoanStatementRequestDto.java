package ru.alex.msapigateway.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.*;


@Setter
@Getter
@Builder
@ToString(exclude = { "passportSeries", "passportNumber" })
@AllArgsConstructor
public class LoanStatementRequestDto {
    private BigDecimal amount;
    private Integer term;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private LocalDate birthdate;
    private String passportSeries;
    private String passportNumber;
}
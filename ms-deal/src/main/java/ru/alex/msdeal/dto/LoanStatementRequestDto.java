package ru.alex.msdeal.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
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
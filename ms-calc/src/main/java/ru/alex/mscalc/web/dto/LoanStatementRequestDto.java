package ru.alex.mscalc.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class LoanStatementRequestDto {

    private BigDecimal amount;
    private Integer term;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String middleName;
    @Email
    private String email;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    @NotNull
    @Size(min = 4, max = 4)
    private String passportSeries;
    @NotNull
    @Size(min = 6, max = 6)
    private String passportNumber;

}
package ru.alex.msstatement.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.alex.msstatement.util.validation.annotation.Adult;
import ru.alex.msstatement.util.validation.annotation.IsLatin;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanStatementRequestDto {

    @NotNull
    @Min(value = 30000, message = "Loan must be under {value}")
    private BigDecimal amount;

    @Min(value = 6, message = "Loan term must be equal or under {value}")
    @NotNull(message = "Term must exists")
    @Positive(message = "Term must be equal or under 6")
    private Integer term;

    @NotBlank
    @Size(min = 2, max = 30)
    @IsLatin
    private String firstName;

    @IsLatin
    private String middleName;

    @NotBlank
    @Size(min = 2, max = 30)
    @IsLatin
    private String lastName;

    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotBlank(message = "Email can't be empty")
    private String email;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Adult
    private LocalDate birthdate;

    @NotBlank(message = "Passport series can not be null")
    @Pattern(regexp = "^\\d{4}$", message = "Passport series must consist in 4 digits")
    private String passportSeries;

    @NotBlank(message = "Passport number can not be null")
    @Pattern(regexp = "^\\d{6}$", message = "Passport number must consist in 6 digits")
    private String passportNumber;
}
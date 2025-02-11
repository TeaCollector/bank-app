package ru.alex.mscalc.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import ru.alex.mscalc.entity.constant.Gender;
import ru.alex.mscalc.entity.constant.MaritalStatus;
import ru.alex.mscalc.util.validation.annotation.IsLatin;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@Builder
@ToString(of = {"amount", "term", "firstName", "lastName",
        "gender", "birthdate", "maritalStatus", "isSalaryClient",
        "isInsuranceEnabled", "employment"})
public class ScoringDataDto {

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

    @NotBlank
    @Size(min = 2, max = 30)
    @IsLatin
    private String lastName;

    @IsLatin
    private String middleName;

    @NotNull
    private Gender gender;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;

    @NotBlank(message = "Passport series can not be null")
    @Pattern(regexp = "^\\d{4}$", message = "Passport series must consist in 4 digits")
    private String passportSeries;

    @NotBlank(message = "Passport number can not be null")
    @Pattern(regexp = "^\\d{6}$", message = "Passport number must consist in 6 digits")
    private String passportNumber;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate passportIssueDate;

    private String passportIssueBranch;
    @NotNull
    private MaritalStatus maritalStatus;

    private Integer dependentAmount;

    @NotNull
    private EmploymentDto employment;

    private String account;

    @NotNull
    private Boolean isInsuranceEnabled;

    @NotNull
    private Boolean isSalaryClient;
}

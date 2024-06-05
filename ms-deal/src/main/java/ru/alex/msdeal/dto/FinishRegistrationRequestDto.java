package ru.alex.msdeal.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import ru.alex.msdeal.entity.constant.Gender;
import ru.alex.msdeal.entity.constant.MaritalStatus;


@Data
@Builder
public class FinishRegistrationRequestDto {

    private Gender gender;
    private MaritalStatus maritalStatus;
    private Integer dependentAmount;
    private LocalDate passportIssueDate;
    private String passportIssueBranch;
    private EmploymentDto employmentDto;
    private String accountNumber;
}

package ru.alex.msdeal.dto;

import java.time.LocalDate;
import lombok.*;
import ru.alex.msdeal.entity.constant.Gender;
import ru.alex.msdeal.entity.constant.MaritalStatus;


@Setter
@Getter
@ToString(of = { "gender", "maritalStatus", "employmentDto" })
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinishRegistrationRequestDto {

    private Gender gender;
    private MaritalStatus maritalStatus;
    private Integer dependentAmount;
    private LocalDate passportIssueDate;
    private String passportIssueBranch;
    private EmploymentDto employmentDto;
    private String accountNumber;
}

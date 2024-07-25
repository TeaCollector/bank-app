package ru.alex.msapigateway.dto;

import java.time.LocalDate;
import lombok.*;
import ru.alex.msapigateway.dto.constant.Gender;
import ru.alex.msapigateway.dto.constant.MaritalStatus;


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

package ru.alex.mscalc.entity;

import lombok.*;
import ru.alex.mscalc.entity.constant.Gender;
import ru.alex.mscalc.entity.constant.MaritalStatus;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
public class Client {

    private String email;
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate birthDate;
    private String passportSeries;
    private String passportNumber;
    private Gender gender;
    private MaritalStatus maritalStatus;
    private LocalDate passportIssueDate;
    private String passportIssueBranch;
}

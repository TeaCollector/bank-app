package ru.alex.msdeal.entity;

import java.time.LocalDate;
import java.util.UUID;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Passport {

    private UUID id;
    private String series;
    private String number;
    private String issueBranch;
    private LocalDate issueDate;

}

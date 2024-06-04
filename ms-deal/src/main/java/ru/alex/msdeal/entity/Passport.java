package ru.alex.msdeal.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class Passport {

    private String series;
    private String number;
    private String issueBranch;
    private Date issueDate;

}

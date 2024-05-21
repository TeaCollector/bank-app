package ru.alex.mscalc.web.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class EmploymentDto {

    // todo здесь енам
    private String employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    // todo здесь енам
    private String position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}

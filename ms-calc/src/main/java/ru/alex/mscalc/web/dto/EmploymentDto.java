package ru.alex.mscalc.web.dto;

import lombok.Getter;
import ru.alex.mscalc.entity.constant.EmploymentStatus;
import ru.alex.mscalc.entity.constant.Position;

import java.math.BigDecimal;

@Getter
public class EmploymentDto {

    // todo здесь енам
    private EmploymentStatus employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    // todo здесь енам
    private Position position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}

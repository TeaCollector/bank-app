package ru.alex.msdeal.dto;

import java.math.BigDecimal;
import lombok.*;
import ru.alex.msdeal.entity.constant.EmploymentPosition;
import ru.alex.msdeal.entity.constant.EmploymentStatus;


@Getter
@Setter
@Builder
@ToString(of = {"employmentStatus", "employerInn", "position", "workExperienceTotal", "workExperienceCurrent"})
public class EmploymentDto {

    private EmploymentStatus employmentStatus;
    private String employerInn;
    private BigDecimal salary;
    private EmploymentPosition position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}

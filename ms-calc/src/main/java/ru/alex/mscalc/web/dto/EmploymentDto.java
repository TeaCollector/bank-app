package ru.alex.mscalc.web.dto;

import lombok.Builder;
import lombok.Data;
import ru.alex.mscalc.entity.constant.EmploymentStatus;
import ru.alex.mscalc.entity.constant.Position;

import java.math.BigDecimal;

@Data
@Builder
public class EmploymentDto {

    private EmploymentStatus employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    private Position position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}

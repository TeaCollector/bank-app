package ru.alex.msdeal.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;
import ru.alex.msdeal.entity.constant.EmploymentPosition;
import ru.alex.msdeal.entity.constant.EmploymentStatus;


@Data
@Builder
public class EmploymentDto {

    private EmploymentStatus employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    private EmploymentPosition position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}

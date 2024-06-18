package ru.alex.msstatement.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;
import ru.alex.msstatement.dto.constant.EmploymentStatus;
import ru.alex.msstatement.dto.constant.Position;


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

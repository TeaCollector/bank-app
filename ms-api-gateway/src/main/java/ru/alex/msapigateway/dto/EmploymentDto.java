package ru.alex.msapigateway.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.alex.msapigateway.dto.constant.EmploymentPosition;
import ru.alex.msapigateway.dto.constant.EmploymentStatus;


@Getter
@Setter
@Builder
@ToString(of = { "employmentStatus", "employerInn", "position", "workExperienceTotal", "workExperienceCurrent" })
public class EmploymentDto {

    private EmploymentStatus employmentStatus;
    private String employerInn;
    private BigDecimal salary;
    private EmploymentPosition position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}

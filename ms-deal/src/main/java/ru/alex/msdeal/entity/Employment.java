package ru.alex.msdeal.entity;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.alex.msdeal.entity.constant.EmploymentPosition;
import ru.alex.msdeal.entity.constant.EmploymentStatus;


@Getter
@Setter
@AllArgsConstructor
@Builder
public class Employment {

    private UUID id;
    private EmploymentStatus employmentStatus;
    private String employerInn;
    private BigDecimal salary;
    private EmploymentPosition employmentsPosition;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;

}

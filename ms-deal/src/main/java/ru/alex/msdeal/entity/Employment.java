package ru.alex.msdeal.entity;

import java.math.BigDecimal;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.alex.msdeal.entity.constant.EmploymentPosition;
import ru.alex.msdeal.entity.constant.EmploymentStatus;


@Getter
@Setter
@AllArgsConstructor
public class Employment {

    @Enumerated(EnumType.STRING)
    private EmploymentStatus employmentStatus;
    private String employerInn;
    private BigDecimal salary;
    @Enumerated(EnumType.STRING)
    private EmploymentPosition employmentsPosition;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;

}

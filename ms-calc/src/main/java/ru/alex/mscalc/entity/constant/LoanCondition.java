package ru.alex.mscalc.entity.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class LoanCondition {
    private boolean isSalaryClient;
    private boolean isInsuranceEnabled;
}
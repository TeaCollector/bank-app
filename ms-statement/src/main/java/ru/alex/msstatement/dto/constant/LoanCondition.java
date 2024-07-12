package ru.alex.msstatement.dto.constant;

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
package ru.alex.msstatement.dto.constant;

public enum EmploymentStatus {
    WORKER("Работник"),
    EMPLOYEE("Сотрудник"),
    SELF_EMPLOYED("Самозанятый"),
    UNEMPLOYED("Безработный");

    EmploymentStatus(String description) {
    }
}

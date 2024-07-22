package ru.alex.msapigateway.dto.constant;

public enum EmploymentStatus {
    BUSINESS_OWNER("Владелец бизнеса"),
    EMPLOYED("Сотрудник"),
    SELF_EMPLOYED("Самозанятый"),
    UNEMPLOYED("Безработный");

    EmploymentStatus(String description) {
    }
}

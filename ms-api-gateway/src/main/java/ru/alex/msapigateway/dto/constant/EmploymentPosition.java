package ru.alex.msapigateway.dto.constant;

public enum EmploymentPosition {
    WORKER("Рабочий"),
    MID_MANAGER("Мэнэджер - середнячок"),
    TOP_MANAGER("Топ мэнэджер"),
    CFO("Финансовый директор"),
    CEO("Испольнительный директор");

    EmploymentPosition(String description) {
    }
}

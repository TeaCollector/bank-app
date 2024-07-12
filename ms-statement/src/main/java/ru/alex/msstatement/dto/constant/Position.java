package ru.alex.msstatement.dto.constant;

public enum Position {
    ADMINISTRATOR("Администратор"),
    SIMPLE_MANAGER("Мэнэджер"),
    TOP_MANAGER("Топ мэнэджер"),
    CFO("Финансовый директор"),
    CEO("Испольнительный директор");

    Position(String description) {
    }
}

package ru.alex.msdeal.entity.constant;

public enum CreditStatus {
    CALCULATED("Вычисляется"),
    ISSUED("Выпущен");

    private String description;

    CreditStatus(String description) {
        this.description = description;
    }
}

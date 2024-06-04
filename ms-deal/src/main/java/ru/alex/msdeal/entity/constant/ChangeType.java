package ru.alex.msdeal.entity.constant;

public enum ChangeType {
    AUTOMATIC("Автоматическое"),
    MANUAL("Ручное");

    private String description;

    ChangeType(String description) {
        this.description = description;
    }
}

package ru.alex.msdeal.entity.constant;

public enum ApplicationStatus {
    PREAPPROVAL("Почти подтверждено"),
    APPROVED("Подтверждено"),
    CC_DENIED("Отклонено"),
    CC_APPROVED("ПОдтверждено"),
    PREPARE_DOCUMENTS("Подготовка документов"),
    DOCUMENT_CREATED("Создание документов"),
    CLIENT_DENIED("Отказ клиенту"),
    DOCUMENT_SIGNED("Документы подписаны"),
    CREDIT_ISSUED("Кредит выдан");


    private String description;

    ApplicationStatus(String description) {
        this.description = description;
    }
}

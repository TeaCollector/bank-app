package ru.alex.msapigateway.dto;

public enum StatementStatus {
    PREAPPROVAL("Предварительное одобрение"),
    APPROVED("Одобрено"),
    CC_DENIED("Отказ в выдаче кредита"),
    CC_APPROVED("Выдача кредита одобрена"),
    PREPARE_DOCUMENTS("Подготовка документов"),
    DOCUMENT_CREATED("Создание документов"),
    CLIENT_DENIED("Отказ клиента"),
    DOCUMENT_SIGNED("Документы подписаны"),
    CREDIT_ISSUED("Кредит выдан");


    StatementStatus(String description) {
    }
}

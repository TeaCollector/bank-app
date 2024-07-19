package ru.alex.msdeal.entity.constant;

public enum Theme {
    FINISH_REGISTRATION("Окончание регистрации"),
    CREATE_DOCUMENT("Создание документа"),
    SEND_DOCUMENT("Документ отправлен"),
    SEND_SES("Ses-код отправлен"),
    CREDIT_ISSUED("Кредит выдан"),
    STATEMENT_DENIED("Заявка отклонена");

    Theme(String description){}
}

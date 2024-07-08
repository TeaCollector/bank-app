package ru.alex.msdossier.dto.constant;

import lombok.Getter;


@Getter
public enum Theme {
    FINISH_REGISTRATION("Окончание регистрации"),
    CREATE_DOCUMENT("Создание документа"),
    SEND_DOCUMENT("Документ отправлен"),
    SEND_SES("Ses-код отправлен"),
    CREDIT_ISSUED("Кредит выдан"),
    STATEMENT_DENIED("Заявка отклонена");

    private String description;

    Theme(String description) {
    }
}

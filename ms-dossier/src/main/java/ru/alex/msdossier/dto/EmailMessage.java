package ru.alex.msdossier.dto;

import java.util.UUID;
import ru.alex.msdossier.dto.constant.Theme;


public record EmailMessage(String address, Theme theme, UUID statementId, Integer sesCode) {
}

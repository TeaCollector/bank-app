package ru.alex.msdeal.dto;

import java.util.UUID;
import lombok.Builder;
import ru.alex.msdeal.entity.constant.Theme;


@Builder
public record EmailMessage(String address, Theme theme, UUID statementId, Integer sesCode) {
}

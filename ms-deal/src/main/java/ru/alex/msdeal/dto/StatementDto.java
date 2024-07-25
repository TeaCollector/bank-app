package ru.alex.msdeal.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.alex.msdeal.entity.AppliedOffer;
import ru.alex.msdeal.entity.Client;
import ru.alex.msdeal.entity.Credit;
import ru.alex.msdeal.entity.StatusHistory;
import ru.alex.msdeal.entity.constant.StatementStatus;


@Getter
@AllArgsConstructor
public final class StatementDto {
    private final UUID id;

    @JsonIgnore
    private final Client client;

    @JsonIgnore
    private final Credit credit;

    private final StatementStatus status;
    private final Instant creationDate;
    private final AppliedOffer appliedOffer;
    private final Instant signDate;
    private final Integer sesCode;
    private final List<StatusHistory> statusHistory;
}

package ru.alex.msapigateway.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;


@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = { "id" })
public final class StatementDto {
    private final UUID id;

    private final StatementStatus status;
    private final Instant creationDate;
    private final AppliedOffer appliedOffer;
    private final Instant signDate;
    private final Integer sesCode;
    private final List<StatusHistory> statusHistory;
}

package ru.alex.msdeal.entity;

import java.time.Instant;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.alex.msdeal.entity.constant.ChangeType;

@Data
@AllArgsConstructor
@Builder
public class StatusHistory {

    private String status;
    private Instant time;

    @Enumerated(EnumType.STRING)
    private ChangeType changeType;
}

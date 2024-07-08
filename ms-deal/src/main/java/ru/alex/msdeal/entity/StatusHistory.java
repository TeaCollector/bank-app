package ru.alex.msdeal.entity;

import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.alex.msdeal.entity.constant.ChangeType;

@Data
@AllArgsConstructor
@Builder
public class StatusHistory implements Serializable {

    private String status;
    private Instant time;
    private ChangeType changeType;
}

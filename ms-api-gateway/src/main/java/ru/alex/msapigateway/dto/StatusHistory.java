package ru.alex.msapigateway.dto;

import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder
public class StatusHistory implements Serializable {

    private String status;
    private Instant time;
    private ChangeType changeType;
}

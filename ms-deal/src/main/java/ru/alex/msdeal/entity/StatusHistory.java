package ru.alex.msdeal.entity;

import java.time.Instant;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import ru.alex.msdeal.entity.constant.ChangeType;


public class StatusHistory {

    private String status;
    private Instant time;

    @Enumerated(EnumType.STRING)
    private ChangeType changeType;
}

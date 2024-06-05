package ru.alex.msdeal.entity;

import java.time.Instant;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import ru.alex.msdeal.entity.constant.ChangeType;


public class StatusHistory {

    private String status;
    private Instant time;

    @Enumerated(EnumType.STRING)
    private ChangeType changeType;
}

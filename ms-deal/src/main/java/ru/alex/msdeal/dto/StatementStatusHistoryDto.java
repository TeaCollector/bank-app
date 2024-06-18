package ru.alex.msdeal.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.alex.msdeal.entity.StatusHistory;
import ru.alex.msdeal.entity.constant.ChangeType;

@Data
@Builder
@AllArgsConstructor
public class StatementStatusHistoryDto {
    private StatusHistory status;
    private LocalDateTime time;
    private ChangeType changeType;
}

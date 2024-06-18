package ru.alex.msdeal.mapper;

import org.mapstruct.Mapper;
import ru.alex.msdeal.dto.PaymentScheduleElementDto;
import ru.alex.msdeal.entity.PaymentSchedule;

@Mapper(componentModel = "spring")
public interface PaymentScheduleMapper extends Mappable<PaymentSchedule, PaymentScheduleElementDto> {
}

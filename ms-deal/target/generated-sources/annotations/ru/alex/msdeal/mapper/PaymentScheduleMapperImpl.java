package ru.alex.msdeal.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.alex.msdeal.dto.PaymentScheduleElementDto;
import ru.alex.msdeal.entity.PaymentSchedule;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-09T08:58:37+0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class PaymentScheduleMapperImpl implements PaymentScheduleMapper {

    @Override
    public PaymentSchedule toEntity(PaymentScheduleElementDto dto) {
        if ( dto == null ) {
            return null;
        }

        Integer number = null;
        LocalDate date = null;
        BigDecimal totalAmount = null;
        BigDecimal interestPayment = null;
        BigDecimal debtPayment = null;
        BigDecimal remainingDebt = null;

        number = dto.getNumber();
        date = dto.getDate();
        totalAmount = dto.getTotalAmount();
        interestPayment = dto.getInterestPayment();
        debtPayment = dto.getDebtPayment();
        remainingDebt = dto.getRemainingDebt();

        PaymentSchedule paymentSchedule = new PaymentSchedule( number, date, totalAmount, interestPayment, debtPayment, remainingDebt );

        return paymentSchedule;
    }

    @Override
    public PaymentScheduleElementDto toDto(PaymentSchedule entity) {
        if ( entity == null ) {
            return null;
        }

        PaymentScheduleElementDto.PaymentScheduleElementDtoBuilder paymentScheduleElementDto = PaymentScheduleElementDto.builder();

        paymentScheduleElementDto.number( entity.getNumber() );
        paymentScheduleElementDto.date( entity.getDate() );
        paymentScheduleElementDto.totalAmount( entity.getTotalAmount() );
        paymentScheduleElementDto.interestPayment( entity.getInterestPayment() );
        paymentScheduleElementDto.debtPayment( entity.getDebtPayment() );
        paymentScheduleElementDto.remainingDebt( entity.getRemainingDebt() );

        return paymentScheduleElementDto.build();
    }

    @Override
    public List<PaymentScheduleElementDto> toDtoList(List<PaymentSchedule> entities) {
        if ( entities == null ) {
            return null;
        }

        List<PaymentScheduleElementDto> list = new ArrayList<PaymentScheduleElementDto>( entities.size() );
        for ( PaymentSchedule paymentSchedule : entities ) {
            list.add( toDto( paymentSchedule ) );
        }

        return list;
    }

    @Override
    public List<PaymentSchedule> toEntityList(List<PaymentScheduleElementDto> entities) {
        if ( entities == null ) {
            return null;
        }

        List<PaymentSchedule> list = new ArrayList<PaymentSchedule>( entities.size() );
        for ( PaymentScheduleElementDto paymentScheduleElementDto : entities ) {
            list.add( toEntity( paymentScheduleElementDto ) );
        }

        return list;
    }
}

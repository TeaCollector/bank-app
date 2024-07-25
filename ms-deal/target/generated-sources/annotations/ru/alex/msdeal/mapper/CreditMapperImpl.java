package ru.alex.msdeal.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.alex.msdeal.dto.CreditDto;
import ru.alex.msdeal.dto.PaymentScheduleElementDto;
import ru.alex.msdeal.entity.Credit;
import ru.alex.msdeal.entity.PaymentSchedule;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-19T15:37:53+0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class CreditMapperImpl implements CreditMapper {

    @Override
    public CreditDto toDto(Credit entity) {
        if ( entity == null ) {
            return null;
        }

        CreditDto.CreditDtoBuilder creditDto = CreditDto.builder();

        creditDto.amount( entity.getAmount() );
        creditDto.term( entity.getTerm() );
        creditDto.monthlyPayment( entity.getMonthlyPayment() );
        creditDto.rate( entity.getRate() );
        creditDto.psk( entity.getPsk() );
        creditDto.paymentSchedule( paymentScheduleListToPaymentScheduleElementDtoList( entity.getPaymentSchedule() ) );

        return creditDto.build();
    }

    @Override
    public List<CreditDto> toDtoList(List<Credit> entities) {
        if ( entities == null ) {
            return null;
        }

        List<CreditDto> list = new ArrayList<CreditDto>( entities.size() );
        for ( Credit credit : entities ) {
            list.add( toDto( credit ) );
        }

        return list;
    }

    @Override
    public List<Credit> toEntityList(List<CreditDto> entities) {
        if ( entities == null ) {
            return null;
        }

        List<Credit> list = new ArrayList<Credit>( entities.size() );
        for ( CreditDto creditDto : entities ) {
            list.add( toEntity( creditDto ) );
        }

        return list;
    }

    @Override
    public Credit toEntity(CreditDto creditDto) {
        if ( creditDto == null ) {
            return null;
        }

        Credit.CreditBuilder credit = Credit.builder();

        credit.insuranceEnabled( creditDto.getIsInsuranceEnabled() );
        credit.salaryClient( creditDto.getIsSalaryClient() );
        credit.amount( creditDto.getAmount() );
        credit.term( creditDto.getTerm() );
        credit.monthlyPayment( creditDto.getMonthlyPayment() );
        credit.rate( creditDto.getRate() );
        credit.psk( creditDto.getPsk() );
        credit.paymentSchedule( paymentScheduleElementDtoListToPaymentScheduleList( creditDto.getPaymentSchedule() ) );

        return credit.build();
    }

    protected PaymentScheduleElementDto paymentScheduleToPaymentScheduleElementDto(PaymentSchedule paymentSchedule) {
        if ( paymentSchedule == null ) {
            return null;
        }

        PaymentScheduleElementDto.PaymentScheduleElementDtoBuilder paymentScheduleElementDto = PaymentScheduleElementDto.builder();

        paymentScheduleElementDto.number( paymentSchedule.getNumber() );
        paymentScheduleElementDto.date( paymentSchedule.getDate() );
        paymentScheduleElementDto.totalAmount( paymentSchedule.getTotalAmount() );
        paymentScheduleElementDto.interestPayment( paymentSchedule.getInterestPayment() );
        paymentScheduleElementDto.debtPayment( paymentSchedule.getDebtPayment() );
        paymentScheduleElementDto.remainingDebt( paymentSchedule.getRemainingDebt() );

        return paymentScheduleElementDto.build();
    }

    protected List<PaymentScheduleElementDto> paymentScheduleListToPaymentScheduleElementDtoList(List<PaymentSchedule> list) {
        if ( list == null ) {
            return null;
        }

        List<PaymentScheduleElementDto> list1 = new ArrayList<PaymentScheduleElementDto>( list.size() );
        for ( PaymentSchedule paymentSchedule : list ) {
            list1.add( paymentScheduleToPaymentScheduleElementDto( paymentSchedule ) );
        }

        return list1;
    }

    protected PaymentSchedule paymentScheduleElementDtoToPaymentSchedule(PaymentScheduleElementDto paymentScheduleElementDto) {
        if ( paymentScheduleElementDto == null ) {
            return null;
        }

        Integer number = null;
        LocalDate date = null;
        BigDecimal totalAmount = null;
        BigDecimal interestPayment = null;
        BigDecimal debtPayment = null;
        BigDecimal remainingDebt = null;

        number = paymentScheduleElementDto.getNumber();
        date = paymentScheduleElementDto.getDate();
        totalAmount = paymentScheduleElementDto.getTotalAmount();
        interestPayment = paymentScheduleElementDto.getInterestPayment();
        debtPayment = paymentScheduleElementDto.getDebtPayment();
        remainingDebt = paymentScheduleElementDto.getRemainingDebt();

        PaymentSchedule paymentSchedule = new PaymentSchedule( number, date, totalAmount, interestPayment, debtPayment, remainingDebt );

        return paymentSchedule;
    }

    protected List<PaymentSchedule> paymentScheduleElementDtoListToPaymentScheduleList(List<PaymentScheduleElementDto> list) {
        if ( list == null ) {
            return null;
        }

        List<PaymentSchedule> list1 = new ArrayList<PaymentSchedule>( list.size() );
        for ( PaymentScheduleElementDto paymentScheduleElementDto : list ) {
            list1.add( paymentScheduleElementDtoToPaymentSchedule( paymentScheduleElementDto ) );
        }

        return list1;
    }
}

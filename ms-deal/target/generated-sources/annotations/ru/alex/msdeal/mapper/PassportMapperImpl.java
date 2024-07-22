package ru.alex.msdeal.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.alex.msdeal.dto.LoanStatementRequestDto;
import ru.alex.msdeal.entity.Passport;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-19T15:37:53+0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class PassportMapperImpl implements PassportMapper {

    @Override
    public LoanStatementRequestDto toDto(Passport entity) {
        if ( entity == null ) {
            return null;
        }

        LoanStatementRequestDto.LoanStatementRequestDtoBuilder loanStatementRequestDto = LoanStatementRequestDto.builder();

        return loanStatementRequestDto.build();
    }

    @Override
    public List<LoanStatementRequestDto> toDtoList(List<Passport> entities) {
        if ( entities == null ) {
            return null;
        }

        List<LoanStatementRequestDto> list = new ArrayList<LoanStatementRequestDto>( entities.size() );
        for ( Passport passport : entities ) {
            list.add( toDto( passport ) );
        }

        return list;
    }

    @Override
    public List<Passport> toEntityList(List<LoanStatementRequestDto> entities) {
        if ( entities == null ) {
            return null;
        }

        List<Passport> list = new ArrayList<Passport>( entities.size() );
        for ( LoanStatementRequestDto loanStatementRequestDto : entities ) {
            list.add( toEntity( loanStatementRequestDto ) );
        }

        return list;
    }

    @Override
    public Passport toEntity(LoanStatementRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Passport.PassportBuilder passport = Passport.builder();

        passport.series( dto.getPassportSeries() );
        passport.number( dto.getPassportNumber() );

        return passport.build();
    }
}

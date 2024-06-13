package ru.alex.msdeal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.alex.msdeal.dto.LoanStatementRequestDto;
import ru.alex.msdeal.entity.Passport;


@Mapper(componentModel = "spring")
public interface PassportMapper extends Mappable<Passport, LoanStatementRequestDto> {

    @Mappings({
        @Mapping(target = "series", source = "passportSeries"),
        @Mapping(target = "number", source = "passportNumber") })
    Passport toEntity(LoanStatementRequestDto dto);
}

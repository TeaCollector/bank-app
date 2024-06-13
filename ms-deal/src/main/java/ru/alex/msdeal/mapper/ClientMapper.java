package ru.alex.msdeal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.alex.msdeal.dto.LoanStatementRequestDto;
import ru.alex.msdeal.entity.Client;


@Mapper(componentModel = "spring")
public interface ClientMapper extends Mappable<Client, LoanStatementRequestDto> {

    @Mapping(target = "birthdate", source = "birthdate")
    Client toEntity(LoanStatementRequestDto dto);
}

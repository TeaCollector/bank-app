package ru.alex.msdeal.mapper;

import org.mapstruct.Mapper;
import ru.alex.msdeal.dto.StatementDto;
import ru.alex.msdeal.entity.Statement;

@Mapper(componentModel = "spring")
public interface StatementMapper extends Mappable<Statement, StatementDto> {
}

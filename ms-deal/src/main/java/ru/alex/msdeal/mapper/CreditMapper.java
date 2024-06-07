package ru.alex.msdeal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.alex.msdeal.dto.CreditDto;
import ru.alex.msdeal.entity.Credit;


@Mapper(componentModel = "spring")
public interface CreditMapper extends Mappable<Credit, CreditDto> {

    @Mappings({
        @Mapping(target = "insuranceEnabled", source = "isInsuranceEnabled"),
        @Mapping(target = "salaryClient", source = "isSalaryClient")})
    Credit toEntity(CreditDto creditDto);
}
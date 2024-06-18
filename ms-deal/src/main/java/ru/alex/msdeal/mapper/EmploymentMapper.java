package ru.alex.msdeal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.alex.msdeal.dto.EmploymentDto;
import ru.alex.msdeal.entity.Employment;


@Mapper(componentModel = "spring")
public interface EmploymentMapper extends Mappable<Employment, EmploymentDto> {

    @Mapping(target = "employmentsPosition", source = "position")
    Employment toEntity(EmploymentDto employmentDto);

}

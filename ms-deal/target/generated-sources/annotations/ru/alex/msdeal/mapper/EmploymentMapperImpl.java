package ru.alex.msdeal.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.alex.msdeal.dto.EmploymentDto;
import ru.alex.msdeal.entity.Employment;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-19T15:37:53+0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class EmploymentMapperImpl implements EmploymentMapper {

    @Override
    public EmploymentDto toDto(Employment entity) {
        if ( entity == null ) {
            return null;
        }

        EmploymentDto.EmploymentDtoBuilder employmentDto = EmploymentDto.builder();

        employmentDto.employmentStatus( entity.getEmploymentStatus() );
        employmentDto.employerInn( entity.getEmployerInn() );
        employmentDto.salary( entity.getSalary() );
        employmentDto.workExperienceTotal( entity.getWorkExperienceTotal() );
        employmentDto.workExperienceCurrent( entity.getWorkExperienceCurrent() );

        return employmentDto.build();
    }

    @Override
    public List<EmploymentDto> toDtoList(List<Employment> entities) {
        if ( entities == null ) {
            return null;
        }

        List<EmploymentDto> list = new ArrayList<EmploymentDto>( entities.size() );
        for ( Employment employment : entities ) {
            list.add( toDto( employment ) );
        }

        return list;
    }

    @Override
    public List<Employment> toEntityList(List<EmploymentDto> entities) {
        if ( entities == null ) {
            return null;
        }

        List<Employment> list = new ArrayList<Employment>( entities.size() );
        for ( EmploymentDto employmentDto : entities ) {
            list.add( toEntity( employmentDto ) );
        }

        return list;
    }

    @Override
    public Employment toEntity(EmploymentDto employmentDto) {
        if ( employmentDto == null ) {
            return null;
        }

        Employment.EmploymentBuilder employment = Employment.builder();

        employment.employmentsPosition( employmentDto.getPosition() );
        employment.employmentStatus( employmentDto.getEmploymentStatus() );
        employment.employerInn( employmentDto.getEmployerInn() );
        employment.salary( employmentDto.getSalary() );
        employment.workExperienceTotal( employmentDto.getWorkExperienceTotal() );
        employment.workExperienceCurrent( employmentDto.getWorkExperienceCurrent() );

        return employment.build();
    }
}

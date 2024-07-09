package ru.alex.msdeal.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.alex.msdeal.dto.LoanStatementRequestDto;
import ru.alex.msdeal.entity.Client;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-09T08:58:37+0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class ClientMapperImpl implements ClientMapper {

    @Override
    public LoanStatementRequestDto toDto(Client entity) {
        if ( entity == null ) {
            return null;
        }

        LoanStatementRequestDto.LoanStatementRequestDtoBuilder loanStatementRequestDto = LoanStatementRequestDto.builder();

        loanStatementRequestDto.firstName( entity.getFirstName() );
        loanStatementRequestDto.middleName( entity.getMiddleName() );
        loanStatementRequestDto.lastName( entity.getLastName() );
        loanStatementRequestDto.email( entity.getEmail() );
        loanStatementRequestDto.birthdate( entity.getBirthdate() );

        return loanStatementRequestDto.build();
    }

    @Override
    public List<LoanStatementRequestDto> toDtoList(List<Client> entities) {
        if ( entities == null ) {
            return null;
        }

        List<LoanStatementRequestDto> list = new ArrayList<LoanStatementRequestDto>( entities.size() );
        for ( Client client : entities ) {
            list.add( toDto( client ) );
        }

        return list;
    }

    @Override
    public List<Client> toEntityList(List<LoanStatementRequestDto> entities) {
        if ( entities == null ) {
            return null;
        }

        List<Client> list = new ArrayList<Client>( entities.size() );
        for ( LoanStatementRequestDto loanStatementRequestDto : entities ) {
            list.add( toEntity( loanStatementRequestDto ) );
        }

        return list;
    }

    @Override
    public Client toEntity(LoanStatementRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Client.ClientBuilder client = Client.builder();

        client.birthdate( dto.getBirthdate() );
        client.lastName( dto.getLastName() );
        client.firstName( dto.getFirstName() );
        client.middleName( dto.getMiddleName() );
        client.email( dto.getEmail() );

        return client.build();
    }
}

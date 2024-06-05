package ru.alex.mscalc.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.alex.mscalc.dto.ScoringDataDto;
import ru.alex.mscalc.exception.InvalidPassportIssuesException;
import ru.alex.mscalc.exception.OldAgeException;
import ru.alex.mscalc.exception.YoungAgeException;


@Slf4j
@Service
public class ClientService {

    @Value("${app.min-age}")
    private Integer minAge;
    @Value("${app.max-age}")
    private Integer maxAge;

    public void validateData(ScoringDataDto scoringDataDto) {
        if (scoringDataDto.getPassportIssueDate().isAfter(LocalDate.now())) {
            log.warn("Sending data by: {} {} passport issued date was expired ", scoringDataDto.getFirstName(), scoringDataDto.getLastName());
            throw new InvalidPassportIssuesException("Sorry, your passport is not valid");
        }

        var yearOfClient = ChronoUnit.YEARS.between(scoringDataDto.getBirthdate(), LocalDate.now());
        checkAgeIsAcceptable(yearOfClient);
    }

    private void checkAgeIsAcceptable(long yearOfClient) {
        if (yearOfClient < minAge) {
            log.warn("Client too young to take credit");
            throw new YoungAgeException("Sorry, you too young");
        } else if (yearOfClient > maxAge) {
            log.warn("Client too old to take credit");
            throw new OldAgeException("Sorry, you too old");
        }
    }
}

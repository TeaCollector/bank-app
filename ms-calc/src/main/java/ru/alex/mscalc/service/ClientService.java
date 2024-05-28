package ru.alex.mscalc.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.alex.mscalc.exception.InvalidPassportDataException;
import ru.alex.mscalc.exception.OldAgeException;
import ru.alex.mscalc.exception.YoungAgeException;
import ru.alex.mscalc.web.dto.ScoringDataDto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class ClientService {

    @Value("${app.min-age}")
    private Integer minAge;
    @Value("${app.max-age}")
    private Integer maxAge;

    public void validateData(ScoringDataDto scoringDataDto) {
        if (scoringDataDto.getPassportIssueDate().isAfter(LocalDate.now())) {
            throw new InvalidPassportDataException("Sorry, your passport is not valid");
        }

        if (scoringDataDto.getPassportIssueDate().isAfter(LocalDate.now())) {
            throw new InvalidPassportDataException("Sorry, your passport is not valid");
        }

        var yearOfClient = ChronoUnit.YEARS.between(scoringDataDto.getBirthdate(), LocalDate.now());
        checkAgeIsAcceptable(yearOfClient);
    }

    private void checkAgeIsAcceptable(long yearOfClient) {
        if (yearOfClient < minAge) {
            throw new YoungAgeException("Sorry, you too young");
        } else if (yearOfClient > maxAge) {
            throw new OldAgeException("Sorry, you too old");
        }
    }
}

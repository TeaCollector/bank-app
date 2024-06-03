package ru.alex.mscalc.service;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.alex.mscalc.exception.InvalidPassportIssuesException;
import ru.alex.mscalc.exception.OldAgeException;
import ru.alex.mscalc.exception.YoungAgeException;
import ru.alex.mscalc.dto.ScoringDataDto;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = ClientService.class)
class ClientServiceTest {

    @Autowired
    ClientService clientService;

    private ScoringDataDto scoringDataDto;

    @BeforeEach
    void setup() {
        scoringDataDto = getScoringData();
    }

    @Test
    @DisplayName("When data is correct success")
    void success() {
        assertDoesNotThrow(() -> clientService.validateData(scoringDataDto));
    }

    @Test
    @DisplayName("When client's passport issues date invalid throw: InvalidPassportIssuesException")
    void throwInvalidPassportIssuesException() {
        scoringDataDto.setPassportIssueDate(LocalDate.of(2025, 3, 3));

       assertThrows(InvalidPassportIssuesException.class,
           () -> clientService.validateData(scoringDataDto));

    }

    @Test
    @DisplayName("When client too young throw: YoungAgeException")
    void throwYoungAgeException() {
        scoringDataDto.setBirthdate(LocalDate.of(2010, 3, 3));

       assertThrows(YoungAgeException.class,
           () -> clientService.validateData(scoringDataDto));
    }

    @Test
    @DisplayName("When client too old throw: OldAgeException")
    void throwOldAgeException() {
        scoringDataDto.setBirthdate(LocalDate.of(1905, 2, 17));

       assertThrows(OldAgeException.class,
           () -> clientService.validateData(scoringDataDto));
    }

    private ScoringDataDto getScoringData() {
        return ScoringDataDto.builder()
            .birthdate(LocalDate.of(1990, 3,1))
            .passportIssueDate(LocalDate.of(2020, 10, 15))
            .build();
    }
}
package ru.alex.mscalc.service;

import java.time.LocalDate;
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
import ru.alex.mscalc.web.dto.ScoringDataDto;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = ClientService.class)
class ClientServiceTest {

    @Autowired
    ClientService clientService;

    @Test
    @DisplayName("When data is correct success")
    void success() {
        var scoringData = getScoringData();

        assertDoesNotThrow(() -> clientService.validateData(scoringData));
    }

    @Test
    @DisplayName("When client's passport issues date invalid throw: InvalidPassportIssuesException")
    void throwInvalidPassportIssuesException() {
        var scoringData = getScoringData();
        scoringData.setPassportIssueDate(LocalDate.of(2025, 3, 3));

       assertThrows(InvalidPassportIssuesException.class,
           () -> clientService.validateData(scoringData));

    }

    @Test
    @DisplayName("When client too young throw: YoungAgeException")
    void throwYoungAgeException() {
        var scoringData = getScoringData();
        scoringData.setBirthdate(LocalDate.of(2010, 3, 3));

       assertThrows(YoungAgeException.class,
           () -> clientService.validateData(scoringData));
    }

    @Test
    @DisplayName("When client too old throw: OldAgeException")
    void throwOldAgeException() {
        var scoringData = getScoringData();
        scoringData.setBirthdate(LocalDate.of(1905, 2, 17));

       assertThrows(OldAgeException.class,
           () -> clientService.validateData(scoringData));
    }






    private ScoringDataDto getScoringData() {
        return ScoringDataDto.builder()
            .birthdate(LocalDate.of(1990, 3,1))
            .passportIssueDate(LocalDate.of(2020, 10, 15))
            .build();
    }

//{
//  "amount": 300000,
//  "term": 6,
//  "firstName": "Alexandr",
//  "lastName": "Sergeev",
//  "middleName": "Yurievich",
//  "gender": "MALE",
//  "birthdate": "1992-07-31",
//  "passportSeries": "4561",
//  "passportNumber": "123456",
//  "passportIssueDate": "2020-04-01",
//  "passportIssueBranch": "OUFMS Russia",
//  "maritalStatus": "MARRIES",
//  "dependentAmount": 3000,
//  "employment": {
//     "employmentStatus": "EMPLOYEE",
//     "employerINN": "31565562234",
//     "salary": 95000,
//     "position": "TOP_MANAGER",
//     "workExperienceTotal": 50,
//     "workExperienceCurrent": 27
//    },
//  "accountNumber": "342263453245373",
//  "isInsuranceEnabled": false,
//  "isSalaryClient": true
//}
}
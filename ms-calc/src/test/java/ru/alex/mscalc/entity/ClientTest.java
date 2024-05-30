package ru.alex.mscalc.entity;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.alex.mscalc.entity.constant.Gender;
import ru.alex.mscalc.entity.constant.MaritalStatus;
import static org.junit.jupiter.api.Assertions.*;


class ClientTest {

    @Test
    @DisplayName("Correct creating client object")
    void createClientObject() {
        assertDoesNotThrow(this::createClient);
    }

    @Test
    @DisplayName("Correct set client field")
    void setClientField() {
        var client = createClient();
        client.setBirthDate(LocalDate.of(2000, 7, 8));
        client.setEmail("sergeev@mail.com");
        client.setFirstName("Maria");
        client.setMiddleName("Petrovna");
        client.setLastName("Galkova");
        client.setGender(Gender.FEMALE);
        client.setMaritalStatus(MaritalStatus.SINGLE);
        client.setPassportIssueBranch("OUFMS Russia Moscow");
        client.setPassportIssueDate(LocalDate.of(2018, 8, 15));
        client.setPassportSeries("4405");
        client.setPassportNumber("000421");

        assertEquals("sergeev@mail.com", client.getEmail());
        assertEquals(LocalDate.of(2000, 7, 8), client.getBirthDate());
        assertEquals("Maria", client.getFirstName());
        assertEquals("Petrovna", client.getMiddleName());
        assertEquals("Galkova", client.getLastName());
        assertEquals(Gender.FEMALE, client.getGender());
        assertEquals(MaritalStatus.SINGLE, client.getMaritalStatus());
        assertEquals("OUFMS Russia Moscow", client.getPassportIssueBranch());
        assertEquals(LocalDate.of(2018, 8, 15), client.getPassportIssueDate());
        assertEquals("4405", client.getPassportSeries());
        assertEquals("000421", client.getPassportNumber());
    }

    private Client createClient() {
        return Client.builder()
            .birthDate(LocalDate.of(1990, 3, 25))
            .firstName("Alexandr")
            .lastName("Sergeev")
            .middleName("yurievich")
            .email("sasha@mail.com")
            .gender(Gender.MALE)
            .passportSeries("2346")
            .passportNumber("457236")
            .maritalStatus(MaritalStatus.MARRIED)
            .passportIssueDate(LocalDate.of(2020, 4,4))
            .passportIssueBranch("OUFMS Russia")
            .build();
    }
}
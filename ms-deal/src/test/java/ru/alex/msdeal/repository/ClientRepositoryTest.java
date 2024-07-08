package ru.alex.msdeal.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.alex.msdeal.entity.Client;
import ru.alex.msdeal.entity.constant.Gender;
import ru.alex.msdeal.util.DataForTest;
import ru.alex.msdeal.util.PostgresContainer;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


class ClientRepositoryTest extends PostgresContainer {

    @Autowired
    ClientRepository clientRepository;

    @Test
    @DisplayName("Correct saved the client")
    void correctSaveClient() {
        var client = clientRepository.saveAndFlush(DataForTest.getClientEntity());

        assertAll("Client saved correct",
                () -> assertEquals("Petrov", client.getLastName()),
                () -> assertEquals("Petya", client.getFirstName()),
                () -> assertEquals("Petrovich", client.getMiddleName()),
                () -> assertEquals(Gender.MALE, client.getGender()));
    }

    @Test
    @DisplayName("Correct changed info in the client")
    void changedInfo() {
        var savedClient = clientRepository.saveAndFlush(DataForTest.getClientEntity());
        var clientId = savedClient.getId();

        savedClient.setFirstName("Masha");
        savedClient.setLastName("Mashova");
        savedClient.setMiddleName("Markovna");

        clientRepository.save(savedClient);

        var client = clientRepository.findById(clientId).orElseThrow();

        assertAll("Client saved correct",
                () -> assertEquals("Masha", client.getFirstName()),
                () -> assertEquals("Mashova", client.getLastName()),
                () -> assertEquals("Markovna", client.getMiddleName()));
    }

    @Test
    @DisplayName("Email need unique constraint throw exception")
    void correctCheckUniqueConstraint() {
        var client = Client.builder()
                .email("sashaga@gmail.com")
                .lastName("Ivanov")
                .firstName("Ivan")
                .birthdate(LocalDate.of(1992, 5, 31))
                .build();

        assertThrows(Exception.class, () -> clientRepository.saveAndFlush(client));
    }
}
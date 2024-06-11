package ru.alex.msdeal.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.alex.msdeal.entity.Client;
import ru.alex.msdeal.entity.constant.Gender;
import ru.alex.msdeal.util.DataForTest;
import ru.alex.msdeal.util.PostgresTestContainer;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PostgresTestContainer.class)
class ClientRepositoryTest {

    @Autowired
    ClientRepository clientRepository;

    Client client;

    @BeforeEach
    void init() {
        client = clientRepository.save(DataForTest.getClientEntity());
    }

    @Test
    @DisplayName("Correct saved the client")
    void correctSaveClient() {
        assertAll("Client saved correct",
            () -> assertEquals("Sergeev", client.getLastName()),
            () -> assertEquals("Alexandr", client.getFirstName()),
            () -> assertEquals("Yurievich", client.getMiddleName()),
            () -> assertEquals(Gender.MALE, client.getGender()));
    }

    @Test
    @DisplayName("Correct changed info in the client")
    void changedInfo() {
        client.setFirstName("Petya");
        client.setLastName("Petrov");
        client.setMiddleName("Petrovich");

        clientRepository.flush();

        assertAll("Client saved correct",
            () -> assertEquals("Petya", client.getFirstName()),
            () -> assertEquals("Petrov", client.getLastName()),
            () -> assertEquals("Petrovich", client.getMiddleName()));
    }
}
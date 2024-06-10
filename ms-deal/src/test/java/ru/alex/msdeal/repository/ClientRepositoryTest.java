package ru.alex.msdeal.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.alex.msdeal.entity.Client;
import ru.alex.msdeal.entity.Employment;
import ru.alex.msdeal.entity.Passport;
import ru.alex.msdeal.entity.constant.EmploymentPosition;
import ru.alex.msdeal.entity.constant.EmploymentStatus;
import ru.alex.msdeal.entity.constant.Gender;
import ru.alex.msdeal.entity.constant.MaritalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClientRepositoryTest {

    @Autowired
    ClientRepository clientRepository;

    private final UUID clientUuid = UUID.randomUUID();
    private final UUID passpartUuid = UUID.randomUUID();
    private final UUID employmentUuid = UUID.randomUUID();

//    @BeforeEach
//    void init() {
//        clientRepository.save(getClientEntity());
//    }

    @Test
    @DisplayName("Correct saved the client")
    void testJpa() {
        var client = clientRepository.save(getClientEntity());

        assertAll("Client saved correct",
                () -> assertEquals("Sergeev", client.getLastName()),
                () -> assertEquals("Alexandr", client.getFirstName()),
                () -> assertEquals("Yurievich", client.getMiddleName()),
                () -> assertEquals(Gender.MALE, client.getGender()));
    }

    private Client getClientEntity() {
        return Client.builder()
                .id(clientUuid)
                .email("sasha@gmail.com")
                .birthdate(LocalDate.now())
                .lastName("Sergeev")
                .firstName("Alexandr")
                .middleName("Yurievich")
                .employment(Employment.builder()
                        .id(employmentUuid)
                        .employerInn("456243688462")
                        .employmentsPosition(EmploymentPosition.TOP_MANAGER)
                        .salary(BigDecimal.valueOf(350_000))
                        .workExperienceCurrent(9)
                        .workExperienceTotal(60)
                        .employmentStatus(EmploymentStatus.EMPLOYED)
                        .build())
                .dependentAmount(30_000)
                .passport(Passport.builder()
                        .id(passpartUuid)
                        .series("5531")
                        .number("445235")
                        .issueDate(LocalDate.of(2010, 5, 15))
                        .issueBranch("OUFMS Russia")
                        .build())
                .gender(Gender.MALE)
                .maritalStatus(MaritalStatus.MARRIED)
                .statement(null)
                .accountNumber("459230050234")
                .build();
    }

    @BeforeAll
    static void setup() {
        postgres.start();
    }

    @AfterAll
    static void tearDown() {
        postgres.stop();
    }

    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.1-alpine");

    @DynamicPropertySource
    private static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
}
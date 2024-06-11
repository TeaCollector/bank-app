package ru.alex.msdeal.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.alex.msdeal.entity.Statement;
import ru.alex.msdeal.entity.constant.StatementStatus;

import java.time.Instant;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@TestPropertySource(properties = {"spring.flyway.locations=classpath:db/test-migration"})
@Import(DataBaseSettings.class)
class StatementRepositoryTest {

    @Container
    private static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:16.1-alpine");

    @Autowired
    StatementRepository statementRepository;

    Statement statement;

    @BeforeEach
    void setUp() {
        statement = Statement.builder()
                .client(null)
                .creationDate(Instant.now())
                .sesCode(100)
                .statusHistory(null)
                .signDate(Instant.now())
                .credit(null)
                .status(StatementStatus.PREAPPROVAL)
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Save statement")
    void testJpa() {
        statementRepository.save(statement);
    }
}
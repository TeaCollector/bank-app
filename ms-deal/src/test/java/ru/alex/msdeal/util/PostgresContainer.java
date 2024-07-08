package ru.alex.msdeal.util;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
public class PostgresContainer {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.1-alpine")
            .withDatabaseName("bank-app-test")
            .withUsername("test-admin")
            .withPassword("test-admin");

    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        postgres.start();
    }
}

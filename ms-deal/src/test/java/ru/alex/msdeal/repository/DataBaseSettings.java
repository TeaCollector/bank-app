package ru.alex.msdeal.repository;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class DataBaseSettings {

    @Bean
    public Flyway init() {
        var configuration = new FluentConfiguration();
        configuration.locations("classpath:db/test-migration");
        return new Flyway(configuration);
    }
}

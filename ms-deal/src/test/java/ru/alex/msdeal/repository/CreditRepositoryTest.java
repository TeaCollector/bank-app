package ru.alex.msdeal.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.alex.msdeal.util.DataForTest;
import ru.alex.msdeal.util.PostgresContainer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CreditRepositoryTest extends PostgresContainer {

    @Autowired
    CreditRepository creditRepository;

    @Test
    @DisplayName("Correct save credit")
    void saveCredit() {
        assertDoesNotThrow(() -> creditRepository.save(DataForTest.getCreditEntity()));
    }

    @Test
    @DisplayName("Correct working foreign key from credit to statement")
    void correctForeignKey() {
        var credit = creditRepository.findById(DataForTest.getCreditId()).orElseThrow();

        var statement = credit.getStatement();

        assertEquals(DataForTest.getStatementId(), statement.getId());
    }
}
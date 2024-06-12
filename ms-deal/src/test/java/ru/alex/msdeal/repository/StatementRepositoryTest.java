package ru.alex.msdeal.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.alex.msdeal.entity.Statement;
import ru.alex.msdeal.entity.constant.StatementStatus;
import ru.alex.msdeal.util.PostgresContainer;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


class StatementRepositoryTest extends PostgresContainer {

    @Autowired
    StatementRepository statementRepository;

    @Test
    @DisplayName("Correct save statement")
    void correctSaveStatement() {
        var statement = Statement.builder()
                .status(StatementStatus.APPROVED)
                .credit(null)
                .appliedOffer(null)
                .creationDate(Instant.now())
                .sesCode(105)
                .client(null)
                .statusHistory(null)
                .signDate(Instant.now())
                .build();

        assertDoesNotThrow(() -> statementRepository.save(statement));
    }

//    @Test
//    @DisplayName("Correct save statement")
//    void correctSaveStatement() {
//        var statement = Statement.builder()
//                .status(StatementStatus.APPROVED)
//                .credit(null)
//                .appliedOffer(null)
//                .creationDate(Instant.now())
//                .sesCode(105)
//                .client(null)
//                .statusHistory(null)
//                .signDate(Instant.now())
//                .build();
//
//        assertDoesNotThrow(() -> statementRepository.save(statement));
//    }
}
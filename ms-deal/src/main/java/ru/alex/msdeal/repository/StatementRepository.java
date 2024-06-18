package ru.alex.msdeal.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.alex.msdeal.entity.Statement;


public interface StatementRepository extends JpaRepository<Statement, UUID> {
}

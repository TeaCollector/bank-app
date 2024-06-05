package ru.alex.msdeal.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.alex.msdeal.entity.Credit;


public interface CreditRepository extends JpaRepository<Credit, UUID> {
}

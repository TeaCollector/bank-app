package ru.alex.msdeal.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.alex.msdeal.entity.Passport;


public interface PassportRepository extends JpaRepository<Passport, UUID> {
}

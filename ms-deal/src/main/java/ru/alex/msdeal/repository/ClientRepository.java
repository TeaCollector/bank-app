package ru.alex.msdeal.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.alex.msdeal.entity.Client;


public interface ClientRepository extends JpaRepository<Client, UUID> {
}

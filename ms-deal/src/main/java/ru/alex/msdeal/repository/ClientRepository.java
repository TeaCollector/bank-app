package ru.alex.msdeal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alex.msdeal.entity.Client;

import java.util.UUID;


public interface ClientRepository extends JpaRepository<Client, UUID> {
}

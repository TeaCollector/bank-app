package ru.alex.mscalc.repository;

import org.springframework.stereotype.Repository;
import ru.alex.mscalc.service.Client;

@Repository
public class ClientRepository {

    public boolean isSalaryClient(Client client) {
        return true;
    }
}
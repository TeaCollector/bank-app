package ru.alex.mscalc.repository;

import org.springframework.stereotype.Repository;

@Repository
public class ClientRepository {

    public boolean findClientOnEmail(String email) {
        // поиск клиента по email
        return true;
    }
}
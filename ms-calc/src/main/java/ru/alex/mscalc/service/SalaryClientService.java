package ru.alex.mscalc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.alex.mscalc.repository.ClientRepository;

@Service
@RequiredArgsConstructor
public class SalaryClientService {

    private final ClientRepository clientRepository;

    public boolean checking(Client client) {
        return clientRepository.isSalaryClient(client);
    }
}

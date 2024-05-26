package ru.alex.mscalc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.alex.mscalc.repository.ClientRepository;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public boolean findClient(String email) {
        return clientRepository.findClientOnEmail(email);
    }

    public void checkOnAge(String email) {
        clientRepository.findClientOnEmail(email);

    }
}

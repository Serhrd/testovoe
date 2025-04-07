package com.example.swimingPoolTask.Service;

import com.example.swimingPoolTask.Entity.Client;
import com.example.swimingPoolTask.Repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ClientService {
    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Клиент с ID " + id + " не найден"));
    }

    public Client addClient(Client client) {
        validateName(client.getName());
        validateEmail(client.getEmail());
        validatePhone(client.getPhone());
        return clientRepository.save(client);
    }

    public Client updateClient(Client updatedClient) {

        Client existingClient = clientRepository.findById(updatedClient.getId())
                .orElseThrow(() -> new IllegalArgumentException("Клиент с ID " + updatedClient.getId() + " не найден"));

        if (validateName(updatedClient.getName())) {
            existingClient.setName(updatedClient.getName());
        }
        if (validateEmail(updatedClient.getEmail())) {
            existingClient.setEmail(updatedClient.getEmail());
        }
        if (validatePhone(updatedClient.getPhone())) {
            existingClient.setPhone(updatedClient.getPhone());
        }

        return clientRepository.save(existingClient);
    }

    public boolean checkClientExist(Long id) {
        return clientRepository.existsById(id);
    }

    public boolean validateName(String name) {
        if (name == null || !name.matches("[а-яА-Я]{4,15}"))
            throw new IllegalArgumentException("Неверный формат ФИО");
        return true;
    }

    public boolean validateEmail(String email) {
        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,6}$"))
            throw new IllegalArgumentException("Неверный формат email");
        return true;
    }

    public boolean validatePhone(String phone) {
        if (phone == null || !phone.matches("^\\d{10,15}$"))
            throw new IllegalArgumentException("Неверный формат номера телефона");
        return true;
    }
}

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

    // Проверить
    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Клиент с ID " + id + " не найден"));
    }

    public void addClient(Client client) {
        clientRepository.save(client);
    }

    public Client updateClient(Client updatedClient) {
        Client existingClient = clientRepository.findById(updatedClient.getId())
                .orElseThrow(() -> new IllegalArgumentException("Клиент с ID " + updatedClient.getId() + " не найден"));


        if (updatedClient.getName() != null) {
            existingClient.setName(updatedClient.getName());
        }
        if (updatedClient.getEmail() != null) {
            existingClient.setEmail(updatedClient.getEmail());
        }
        if (updatedClient.getPhone() != null) {
            existingClient.setPhone(updatedClient.getPhone());
        }

        return clientRepository.save(existingClient);
    }
    }

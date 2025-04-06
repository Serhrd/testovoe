package com.example.swimingPoolTask.Service;

import com.example.swimingPoolTask.Entity.Client;
import com.example.swimingPoolTask.Repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
    @Mock
    ClientRepository clientRepository;
    @InjectMocks
    ClientService clientService;

    List<Client> clients= new ArrayList<>();

    @BeforeEach
    void setUp(){
        Client client = new Client(1L, "ИмяФИ", "7999999999", "W1@gmail.ru");
        Client client2 = new Client(2L, "ИмяФИ", "7999999999", "W1@gmail.ru");
        Client client3 = new Client(3L, "ИмяФИ", "7999999999", "W1@gmail.ru" );
        clients = Arrays.asList(client, client2, client3);
    }

    @Test
    void findAll() {
        when(clientRepository.findAll()).thenReturn(clients);

        List<Client> result = clientService.findAll();

        assertEquals(clients,result);
        verify(clientRepository, times(1)).findAll();

    }

    @Test
    void getClientById() {
        when(clientRepository.findById(1L)).thenReturn(Optional.ofNullable(clients.get(0)));

        Client foundedClient = clientService.getClientById(1L);

        assertNotNull(foundedClient);
        assertEquals(clients.get(0),foundedClient);
    }

    @Test
    void addClient() {
        clientService.addClient(clients.get(0));
        verify(clientRepository, times(1)).save(clients.get(0));
    }

    @Test
    void updateClient() {
        Client updatedClient = clients.get(0);
        updatedClient.setPhone("81111111111");

        when(clientRepository.findById(1L)).thenReturn(Optional.ofNullable(clients.get(0)));
        when(clientRepository.save(any(Client.class))).thenReturn(updatedClient);

        Client result = clientService.updateClient(updatedClient);
        assertNotNull(result);
        assertEquals(clients.get(0).getId(),result.getId());
        verify(clientRepository, times(1)).save(any(Client.class));

    }
    @Test
    void updateClient_ShouldThrowException_WhenClientNotFound() {
        Client updatedClient = new Client();
        updatedClient.setId(2L);
        updatedClient.setName("Client2");

        when(clientRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            clientService.updateClient(updatedClient);
        });

        assertEquals("Клиент с ID 2 не найден", exception.getMessage());
    }
}
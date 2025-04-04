package com.example.swimingPoolTask.Controller;

import com.example.swimingPoolTask.Entity.Client;
import com.example.swimingPoolTask.Service.ClientService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v0/pool/client")
public class ClientController {

     private final ClientService clientService;
@Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/all")
    public List<Client> getAllClient() {
        System.out.println(clientService.findAll());
        return clientService.findAll();
    }

    @GetMapping("/{id}")
    public Client getClientById(@PathVariable Long id) {
        System.out.println(clientService.getClientById(id).toString());
        return clientService.getClientById(id);
    }

    @PostMapping("/add")
    public void addClient(@RequestBody Client client) {
        clientService.addClient(client);
    }

    @PostMapping("/update")
    public void updateClient(@RequestBody Client newClient) {
        clientService.updateClient(newClient);
    }
}

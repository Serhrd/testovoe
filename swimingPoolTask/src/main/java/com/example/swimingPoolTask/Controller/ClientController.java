package com.example.swimingPoolTask.Controller;

import com.example.swimingPoolTask.Entity.Client;
import com.example.swimingPoolTask.Service.ClientService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Client>> getAllClient() {
        return ResponseEntity.ok(clientService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClientById(id));
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

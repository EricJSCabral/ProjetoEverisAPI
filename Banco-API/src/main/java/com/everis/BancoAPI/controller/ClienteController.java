package com.everis.BancoAPI.controller;

import com.everis.BancoAPI.model.ClienteModel;
import com.everis.BancoAPI.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ClienteController {

    @Autowired
    private ClienteRepository repository;

    @GetMapping(path = "/api/cliente/{codigo}")
    public ResponseEntity consultar(@PathVariable("codigo") Integer codigo){
        return repository.findById(codigo)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(path = "/api/cliente/listar")
    public ResponseEntity<List<ClienteModel>> listarTudo(){
        List<ClienteModel> clientes = (List<ClienteModel>) repository.findAll();
        return ResponseEntity.ok().body(clientes);
    }

    @PostMapping(path = "/api/cliente/salvar")
    public ClienteModel salvar(@RequestBody ClienteModel cliente){
        return repository.save(cliente);
    }

    @DeleteMapping(path = "/api/cliente/delete/{codigo}")
    public void deletar (@PathVariable("codigo") Integer codigo){
        repository.deleteById(codigo);
    }

    @PutMapping(path = "/api/cliente/atualizar")
    public ClienteModel atualizar(@RequestBody ClienteModel cliente){
        return repository.save(cliente);
    }
}

package com.everis.BancoAPI.controller;

import com.everis.BancoAPI.model.ClienteModel;
import com.everis.BancoAPI.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping(path = "/api/cliente")
    public ResponseEntity consultar(@RequestParam("codigo") Integer codigo){
       return clienteService.consultar(codigo);
    }

    @GetMapping(path = "/api/cliente/listar")
    public ResponseEntity<List<?>> listarTudo(){
       return clienteService.listar();
    }

    @PostMapping(path = "/api/cliente/salvar")
    public ResponseEntity<?> salvar(@RequestBody @Valid ClienteModel cliente){
       return clienteService.salvar(cliente);
    }

    @DeleteMapping(path = "/api/cliente/delete")
    public ResponseEntity deletar (@RequestParam("codigo") Integer codigo){
       return clienteService.deletar(codigo);
    }

    @PutMapping(path = "/api/cliente/atualizar")
    public ResponseEntity atualizar(@RequestParam("codigo") Integer codigo, @RequestBody @Valid ClienteModel cliente){
       return  clienteService.atualizar(codigo, cliente);
    }
}

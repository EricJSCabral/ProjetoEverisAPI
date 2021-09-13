package com.everis.BancoAPI.controller;

import com.everis.BancoAPI.model.ContaModel;
import com.everis.BancoAPI.model.TransacaoModel;
import com.everis.BancoAPI.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
public class ContaController {

    @Autowired
    private ContaService contaService;
    
    @GetMapping(path = "/api/conta")
    public HttpEntity<?> consultar(@RequestParam("numero") String numero) {
        return contaService.consultar(numero);
    }

    @GetMapping(path = "/api/conta/listar")
    public ResponseEntity<List<?>> listarTudo() {
       return contaService.listar();
    }

    @PostMapping(path = "/api/conta/salvar")
    public ResponseEntity<?> salvar(@RequestBody @Valid ContaModel conta) {
        return contaService.salvar(conta);
    }


    @DeleteMapping(path = "/api/conta/delete")
    public ResponseEntity deletar(@RequestParam("numero") String numero) {
        return contaService.deletar(numero);
    }

    @PutMapping(path = "/api/conta/atualizar")
    public ResponseEntity atualizar(@RequestParam ("codigo") Integer codigo, @RequestBody @Valid ContaModel conta) {
        return contaService.atualizar(codigo, conta);
    }

    //Método para depositar
    @PutMapping(path = "/api/conta/depositar")
    public ResponseEntity depositar(@RequestParam("numero") String numero,
                                    @RequestBody TransacaoModel transacao) {
       return contaService.depositar(numero, transacao);
    }

    //Método para sacar
    @PutMapping(path = "/api/conta/sacar")
    public ResponseEntity sacar(@RequestParam("numero") String numero,
                                @RequestBody TransacaoModel transacao) {
        return contaService.sacar(numero, transacao);
    }

    //Método para transferências
    @PutMapping(path = "/api/conta/transferir")
    public ResponseEntity transferir(@RequestParam("numero1") String numero1,
                                        @RequestParam ("numero2") String numero2,
                                        @RequestBody TransacaoModel transacao) {
       return contaService.transferir(numero1, numero2, transacao);
    }

    @GetMapping(path = "/api/conta/extrato")
    public ResponseEntity<List<?>> extrato(@RequestParam("numero") String numero) {
        return contaService.extrato(numero);
    }

    //Método para listar todas as operações realizadas
    @GetMapping(path = "/api/conta/extrato/all")
    public ResponseEntity<List<?>> exibirExtratos() {
        return contaService.exibirExtratos();
    }
}


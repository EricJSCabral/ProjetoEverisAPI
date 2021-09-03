package com.everis.BancoAPI.controller;

import com.everis.BancoAPI.model.ContaModel;
import com.everis.BancoAPI.model.OperacoesModel;
import com.everis.BancoAPI.repository.OperacoesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class OperacoesController {

    @Autowired
    private OperacoesRepository repository;



    public OperacoesModel salvarOperacao(ContaModel c1, double valor, String tipo){
        OperacoesModel op = new OperacoesModel(c1.getNumero(), valor, tipo);
        return repository.save(op);
    }


    public OperacoesModel salvarOperacao(ContaModel c1, double valor, String tipo, int taxa){
        OperacoesModel op = new OperacoesModel(c1.getNumero(), valor, tipo, taxa);
        return repository.save(op);
    }
}

package com.everis.BancoAPI.controller;

import com.everis.BancoAPI.model.ContaModel;
import com.everis.BancoAPI.model.Taxa;
import com.everis.BancoAPI.model.TipoConta;
import com.everis.BancoAPI.repository.ContaRepository;
import com.everis.BancoAPI.repository.TaxaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class TaxaController {

    @Autowired
    private TaxaRepository repository;

    public int aplicarTaxa(Optional<ContaModel> conta){
        Taxa taxa = new Taxa();

        conta.map(busca -> {
        if (busca.getTipo().equals(TipoConta.FISICA))
        {
            taxa.setTaxa(10);
        } else if (busca.getTipo().equals(TipoConta.JURIDICA)) {
            taxa.setTaxa(10);
        } else if (busca.getTipo().equals(TipoConta.GOVERNAMENTAL)){
            taxa.setTaxa(20);
        }
            return taxa.getTaxa();
        }).orElse(null);
        return taxa.getTaxa();
    }
}

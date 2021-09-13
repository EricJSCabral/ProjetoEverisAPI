package com.everis.BancoAPI.service;

import com.everis.BancoAPI.model.ContaModel;
import com.everis.BancoAPI.model.TaxaModel;
import com.everis.BancoAPI.model.TipoConta;
import com.everis.BancoAPI.repository.TaxaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class TaxaService {

    @Autowired
    private TaxaRepository repository;

    public int aplicarTaxa(Optional<ContaModel> conta){
        TaxaModel taxa = new TaxaModel();

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

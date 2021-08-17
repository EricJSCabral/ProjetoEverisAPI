package com.everis.BancoAPI.controller;

import com.everis.BancoAPI.model.ClienteModel;
import com.everis.BancoAPI.model.ContaModel;
import com.everis.BancoAPI.model.Taxa;
import com.everis.BancoAPI.model.TipoConta;
import com.everis.BancoAPI.repository.TaxaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;


class TaxaControllerTest {
    @Autowired
    private TaxaController taxaController;

    @MockBean
    private TaxaRepository taxaRepository;

    @Test
    void aplicarTaxa() {
                ClienteModel cli = new ClienteModel(1, "Eric", "49026915822", "Rua paulista",
                        "44556677");
                ContaModel c = new ContaModel(1, 4576, 33567, 2, TipoConta.FISICA,
                        cli, 500.0, 0);

                Taxa taxa = new Taxa();

                if (c.getSaques() == 0) {
                    if (c.getTipo().equals(TipoConta.FISICA)) {
                        taxa.setTaxa(10);
                    } else if (c.getTipo().equals(TipoConta.JURIDICA)) {
                        taxa.setTaxa(10);
                    } else if (c.getTipo().equals(TipoConta.GOVERNAMENTAL)) {
                        taxa.setTaxa(20);
                    }
                }

                assertThat(taxa.getTaxa()).isEqualTo(10);
            }
}
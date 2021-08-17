package com.everis.BancoAPI.controller;

import com.everis.BancoAPI.model.OperacoesModel;
import com.everis.BancoAPI.model.TipoOperacao;
import com.everis.BancoAPI.repository.OperacoesRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@WebAppConfiguration
class OperacoesControllerTest {

    @Autowired
    private OperacoesController operacoesController;

    @MockBean
    private OperacoesRepository operacoesRepository;

    @Test
    void salvarOperacaoDeveManterRegistro() {
        OperacoesModel op = new OperacoesModel(1, 100.0, TipoOperacao.SAQUE.getDesc());
        this.operacoesRepository.save(op);
        assertThat(op.getCod_operacao()).isNotNull();
        assertThat(op.getId_conta1()).isEqualTo(1);
        assertThat(op.getId_conta2()).isEqualTo(0);
        assertThat(op.getValor()).isEqualTo(100);
        assertThat(op.getTipo()).isEqualTo(TipoOperacao.SAQUE.getDesc());
    }

    @Test
    void SalvarOperacaoDeveManterRegistroTransferencias() {
        OperacoesModel op = new OperacoesModel(1, 2, 100.0);
        this.operacoesRepository.save(op);
        assertThat(op.getCod_operacao()).isNotNull();
        assertThat(op.getId_conta1()).isEqualTo(1);
        assertThat(op.getId_conta2()).isEqualTo(2);
        assertThat(op.getValor()).isEqualTo(100);
        assertThat(op.getTipo()).isEqualTo(TipoOperacao.TRANSFERENCIA.getDesc());

    }
}
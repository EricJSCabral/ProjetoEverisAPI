package com.everis.BancoAPI.service;

import com.everis.BancoAPI.model.*;
import com.everis.BancoAPI.repository.OperacoesRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class OperacoesServiceTest {

    @Mock
    private OperacoesRepository repository;

    @InjectMocks
    private OperacoesService operacoesService;

    public OperacoesModel operacoes(){
        OperacoesModel op = new OperacoesModel();
        op.setCod_operacao(1);
        op.setNumeroConta("1234");
        op.setTipo("Depósito");
        op.setValor(100.0);
        op.setTaxa(0);
        op.setData(LocalDateTime.now().toString());
        return op;
    }

    private ClienteModel cliente(){
        ClienteModel c = new ClienteModel(1, "Eric", "49026915829", "Rua paulista, 155",
                "11944556677");
        return c;
    }

    private ContaModel conta() {
        ContaModel conta = new ContaModel(1, "45764", "3356", "2", TipoConta.FISICA,
                cliente(), 500.0, 0);

        return conta;
    }

    @Test
    public void salvarDeveSalvarOperacao(){
        OperacoesModel op = operacoes();
        ContaModel conta = new ContaModel();

        when(repository.save(any(OperacoesModel.class))).thenReturn(new OperacoesModel());

        OperacoesModel salvo = operacoesService.salvarOperacao(conta, 1000, TipoOperacao.DEPOSITO.getDesc());

        assertThat(salvo.getCod_operacao()).isEqualTo(op.getCod_operacao());
    }

    @Test
    public void salvarDeveSalvarOperacaoSaque(){
        OperacoesModel op = operacoes();
        ContaModel conta = new ContaModel();

        when(repository.save(any(OperacoesModel.class))).thenReturn(new OperacoesModel());

        OperacoesModel salvo = operacoesService.salvarOperacao(conta, 1000, TipoOperacao.DEPOSITO.getDesc(), op.getTaxa());

        assertThat(salvo.getCod_operacao()).isEqualTo(op.getCod_operacao());
    }
}
package com.everis.BancoAPI.repository;

import com.everis.BancoAPI.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class OperacoesRepositoryTest {

    @Autowired
    private OperacoesRepository repository;

    @Autowired
    private TestEntityManager testEntityManager;

   public OperacoesModel operacoes(){
       OperacoesModel op = new OperacoesModel();
       op.setNumeroConta("1234");
       op.setTipo(TipoOperacao.DEPOSITO.getDesc());
       op.setValor(100.0);
       op.setTaxa(10);
       op.setData("29-02-2000");
       return op;
   }

    @Test
    public void buscarContaExistente(){
        OperacoesModel operacao = operacoes();

        operacao = testEntityManager.persistAndFlush(operacao);

        assertThat(repository.findAllByNumeroConta(operacao.getNumeroConta()).contains(operacao));
   }
}
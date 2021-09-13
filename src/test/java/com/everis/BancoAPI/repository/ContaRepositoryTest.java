package com.everis.BancoAPI.repository;

import com.everis.BancoAPI.model.ClienteModel;
import com.everis.BancoAPI.model.ContaModel;
import com.everis.BancoAPI.model.TipoConta;
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
public class ContaRepositoryTest {

    @Autowired
    private ContaRepository repository;

    @Autowired
    private TestEntityManager testEntityManager;

    public ClienteModel cliente(){
        ClienteModel cliente = new ClienteModel();
        cliente.setNome("Carlos");
        cliente.setCpf("49026915829");
        cliente.setEndereco("Rua Palestra Itália, 322");
        cliente.setTelefone("11912345678");

        return cliente;
    }

    public ContaModel conta(){

        ClienteModel cliente = cliente();
        cliente = testEntityManager.persist(cliente);

        ContaModel conta = new ContaModel();
        conta.setAgencia("12345");
        conta.setNumero("1234");
        conta.setDigitoVerificador("1");
        conta.setTipo(TipoConta.FISICA);
        conta.setCliente(cliente);
        conta.setSaldo(0.0);
        conta.setSaques(0);
        return conta;
    }

    @Test
    public void buscarContaExistente(){
       ContaModel conta = conta();

        conta = testEntityManager.persistAndFlush(conta);

        assertThat(repository.findByNumero(conta.getNumero()).get()).isEqualTo(conta);
    }
}
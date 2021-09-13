package com.everis.BancoAPI.repository;

import com.everis.BancoAPI.model.ClienteModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository repository;

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
    @Test
    public void buscarCPFExistente(){
        ClienteModel cliente = cliente();

        cliente = testEntityManager.persistAndFlush(cliente);

        assertThat(repository.findByCpf(cliente.getCpf()).get()).isEqualTo(cliente);
    }
}
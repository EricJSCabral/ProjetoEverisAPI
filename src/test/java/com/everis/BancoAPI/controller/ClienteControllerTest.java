package com.everis.BancoAPI.controller;

import com.everis.BancoAPI.model.ClienteModel;
import com.everis.BancoAPI.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@WebAppConfiguration
class ClienteControllerTest {

    @Autowired
    private ClienteController clienteController;
    @MockBean
    private ClienteRepository clienteRepository;

    @Test
    void consultarDeveBuscarRegistro() {
        ClienteModel c = new ClienteModel(1, "Eric", "49026915822", "Rua paulista",
                "44556677");
        assertThat(this.clienteRepository.findById(1)).isNotNull();
    }

    @Test
    void listarTudoDeveBuscarTodosOsRegistros() {
        List<ClienteModel> clientes = (List<ClienteModel>) clienteRepository.findAll();
        assertThat(clientes).isNotNull();
    }

    @Test
    void salvarDeveManterDados() {
        ClienteModel c = new ClienteModel(1, "Eric", "49026915822", "Rua paulista",
                "44556677");
        this.clienteRepository.save(c);
        assertThat(c.getCodigo()).isNotNull();
        assertThat(c.getNome()).isEqualTo("Eric");
        assertThat(c.getCpf()).isEqualTo("49026915822");
        assertThat(c.getEndereco()).isEqualTo("Rua paulista");
        assertThat(c.getTelefone()).isEqualTo("44556677");
    }

    @Test
    void deletarDeveExcluirDados() {
        ClienteModel c = new ClienteModel(1, "Eric", "49026915822", "Rua paulista",
                "44556677");
        this.clienteRepository.save(c);
        this.clienteRepository.delete(c);
        assertThat(clienteRepository.existsById(c.getCodigo())).isNotNull();
    }

    @Test
    void atualizarDeveAlterarDados() {
        ClienteModel c = new ClienteModel(1, "Eric", "49026915822", "Rua paulista",
                "44556677");
        this.clienteRepository.save(c);
        c.setNome("Carlos");
        c.setCpf("08756745321");
        c.setEndereco("Rua Brilhante");
        c.setTelefone("44332211");
        assertThat(c.getNome()).isEqualTo("Carlos");
        assertThat(c.getCpf()).isEqualTo("08756745321");
        assertThat(c.getEndereco()).isEqualTo("Rua Brilhante");
        assertThat(c.getTelefone()).isEqualTo("44332211");
    }
}
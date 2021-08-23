package com.everis.BancoAPI.controller;

import com.everis.BancoAPI.model.*;
import com.everis.BancoAPI.repository.ContaRepository;
import com.everis.BancoAPI.repository.OperacoesRepository;
import org.testng.annotations.Test;
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

class ContaControllerTest {

    @Autowired
    private ContaController contaController;

    @MockBean
    private ContaRepository contaRepository;

    @MockBean
    private OperacoesRepository operacoesRepository;

//    @BeforeEach
//    public void setup(){
//        RestAssuredMockMvc.standaloneSetup(this.contaController);
//    }

    @Test
    public void consultarDeveBuscarRegistro() {
        ClienteModel cli = new ClienteModel(1, "Eric", "49026915822", "Rua paulista",
                "44556677");
        ContaModel c = new ContaModel(1, 4576, 33567, 2, TipoConta.FISICA,
                cli, 500.0, 5);

        assertThat(this.contaRepository.findById(1)).isNotNull();
    }

    @Test
    public void listarTudoDeveBuscarTodosOsRegistros() {
        List<ContaModel> contas = (List<ContaModel>) contaRepository.findAll();
        assertThat(contas).isNotNull();
    }

    @Test
    public void salvarDeveManterDados() {
        ClienteModel cli = new ClienteModel(1, "Eric", "49026915822", "Rua paulista",
                "44556677");
        ContaModel c = new ContaModel(1, 4576, 33567, 2, TipoConta.FISICA,
                cli, 500.0, 5);
        this.contaRepository.save(c);
        assertThat(c.getCodigo()).isNotNull();
        assertThat(c.getAgencia()).isEqualTo(4576);
        assertThat(c.getNumero()).isEqualTo(33567);
        assertThat(c.getDigitoVerificador()).isEqualTo(2);
        assertThat(c.getTipo()).isEqualTo(TipoConta.FISICA);
        assertThat(c.getCliente()).isNotNull();
        assertThat(c.getSaldo()).isEqualTo(500.0);
        assertThat(c.getSaques()).isEqualTo(5);
    }

    @Test
    void deletarDeveExcluirDados() {
        ClienteModel cli = new ClienteModel(1, "Eric", "49026915822", "Rua paulista",
                "44556677");
        ContaModel c = new ContaModel(1, 4576, 33567, 2, TipoConta.FISICA,
                cli, 500.0, 5);
        this.contaRepository.save(c);
        this.contaRepository.delete(c);
        assertThat(contaRepository.existsById(c.getCodigo())).isNotNull();
    }

    @Test
    void atualizarDeveAlterarDados() {
        ClienteModel cli = new ClienteModel(1, "Eric", "49026915822", "Rua paulista",
                "44556677");
        ClienteModel cli1 = new ClienteModel(2, "João", "49026915822", "Rua paulista",
                "44556677");
        ContaModel c = new ContaModel(1, 4576, 33567, 2, TipoConta.FISICA,
                cli, 500.0, 5);
        this.contaRepository.save(c);

        c.setAgencia(1234);
        c.setNumero(44332);
        c.setDigitoVerificador(1);
        c.setTipo(TipoConta.JURIDICA);
        c.setCliente(cli1);
        c.setSaldo(100.0);
        c.setSaques(250);

        assertThat(c.getAgencia()).isEqualTo(1234);
        assertThat(c.getNumero()).isEqualTo(44332);
        assertThat(c.getDigitoVerificador()).isEqualTo(1);
        assertThat(c.getTipo()).isEqualTo(TipoConta.JURIDICA);
        assertThat(c.getCliente().getNome()).isEqualTo("João");
        assertThat(c.getSaldo()).isEqualTo(100);
        assertThat(c.getSaques()).isEqualTo(250);

    }

    @Test
    void depositarDeveSomarValor() {
        ClienteModel cli = new ClienteModel(1, "Eric", "49026915822", "Rua paulista",
                "44556677");
        ContaModel c = new ContaModel(1, 4576, 33567, 2, TipoConta.FISICA,
                cli, 500.0, 5);

        double valor = 100;
        this.contaRepository.save(c);
        assertThat(contaRepository.findById(1)).isNotNull();
        c.setSaldo(c.getSaldo()+100);
        this.contaRepository.save(c);
        assertThat(c.getSaldo()).isEqualTo(600);
    }

    @Test
    void sacarDeveSubtrairValorETaxa() {
        ClienteModel cli = new ClienteModel(1, "Eric", "49026915822", "Rua paulista",
                "44556677");
        ContaModel c = new ContaModel(1, 4576, 33567, 2, TipoConta.FISICA,
                cli, 500.0, 0);
        Taxa t = new Taxa();
        t.setTaxa(0);
        if (c.getSaques() == 0) {
            t.setTaxa(10);
        }
        double valor = 100;
        this.contaRepository.save(c);
        assertThat(contaRepository.findById(1)).isNotNull();
        c.setSaldo(c.getSaldo() - valor - t.getTaxa());
        this.contaRepository.save(c);
        assertThat(c.getSaldo()).isEqualTo(390);
    }

    @Test
    void transferirDeveTransferirValor() {
        ClienteModel cli = new ClienteModel(1, "Eric", "49026915822", "Rua paulista",
                "44556677");
        ContaModel c = new ContaModel(1, 4576, 33567, 2, TipoConta.FISICA,
                cli, 500.0, 5);
        ContaModel c2 = new ContaModel(2, 6655, 33123, 1, TipoConta.FISICA,
                cli, 200.0, 2);

        this.contaRepository.save(c);
        this.contaRepository.save(c2);
        assertThat(contaRepository.findById(1)).isNotNull();
        assertThat(contaRepository.findById(2)).isNotNull();
        double valor = 100;
        c.setSaldo(c.getSaldo() - valor);
        c2.setSaldo(c2.getSaldo() + valor);
        this.contaRepository.save(c);
        this.contaRepository.save(c2);
        assertThat(c.getSaldo()).isEqualTo(400);
        assertThat(c2.getSaldo()).isEqualTo(300);

    }

    @Test
    void exibirExtratosDeveBuscarTodosOsRegistros() {
        List<OperacoesModel> op = (List<OperacoesModel>) operacoesRepository.findAll();
        assertThat(operacoesRepository.findAll()).isNotNull();
    }
}
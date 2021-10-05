package com.everis.BancoAPI.service;

import com.everis.BancoAPI.model.*;
import com.everis.BancoAPI.repository.ContaRepository;
import com.everis.BancoAPI.repository.OperacoesRepository;
//import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private OperacoesRepository operacoesRepository;

    @InjectMocks
    private ContaService contaService;

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

    private ContaModel conta2() {
        ContaModel conta = new ContaModel(1, "45764", "4478", "2", TipoConta.FISICA,
                cliente(), 500.0, 0);

        return conta;
    }

    @Test
    public void salvarDeveSalvarConta(){
        ContaModel conta = conta();

        when(contaRepository.save(any(ContaModel.class))).thenReturn(new ContaModel());

        ContaModel expected = contaService.salvar(conta);

        assertThat(conta.getCodigo().equals(expected));
    }

    @Test
    public void consultarContaDeveRetornarConta(){
        ContaModel conta = conta();

        when(contaRepository.findByNumero(conta.getNumero())).thenReturn(Optional.of(conta));

        ContaModel expected = contaService.consultar(conta.getNumero());

        assertThat(conta.getNumero().equals(expected));
        verify(contaRepository).findByNumero(conta.getNumero());
    }

    @Test
    public void listarContaJaExiste(){
        List<ContaModel> conta = new ArrayList();

        conta.add(new ContaModel());

        given(contaRepository.findAll()).willReturn(conta);

        List<ContaModel> expected = contaService.listar();

        assertEquals(conta, expected);
        verify(contaRepository).findAll();
    }

    @Test
    public void deletarDeveDeletarConta(){
        ContaModel conta = conta();

        when(contaRepository.findByNumero(conta.getNumero())).thenReturn(Optional.of(conta));

        contaService.deletar(conta.getNumero());

        verify(contaRepository).deleteById(conta.getCodigo());
    }

    @Test
    public void atualizarDeveAtualizarConta(){
        ContaModel conta = conta();
        ContaModel c2 = conta();
        c2.setNumero("6789");

        given(contaRepository.findById(conta.getCodigo())).willReturn(Optional.of(conta));

        contaService.atualizar(conta.getCodigo(), c2);
        verify(contaRepository).save(c2);
        verify(contaRepository).findById(conta.getCodigo());
    }


//    @Test
//    public void consultarDeveBuscarRegistro() {
//
//        ContaModel c = conta();
//        assertThat(this.contaRepository.findById(1)).isNotNull();
//    }
//
//    @Test
//    public void listarTudoDeveBuscarTodosOsRegistros() {
//        List<ContaModel> contas = (List<ContaModel>) contaRepository.findAll();
//        assertThat(contas).isNotNull();
//    }
//
//    @Test
//    public void salvarDeveManterDados() {
//        ContaModel c = conta();
//        this.contaRepository.save(c);
//        assertThat(c.getCodigo()).isNotNull();
//        assertThat(c.getAgencia()).isEqualTo("45764");
//        assertThat(c.getNumero()).isEqualTo("3356");
//        assertThat(c.getDigitoVerificador()).isEqualTo("2");
//        assertThat(c.getTipo()).isEqualTo(TipoConta.FISICA);
//        assertThat(c.getCliente()).isNotNull();
//        assertThat(c.getSaldo()).isEqualTo(500.0);
//        assertThat(c.getSaques()).isEqualTo(0);
//    }
//
//    @Test
//    public void deletarDeveExcluirDados() {
//        ContaModel c = conta();
//        this.contaRepository.save(c);
//        this.contaRepository.delete(c);
//        assertThat(contaRepository.existsById(c.getCodigo())).isNotNull();
//    }
//
//    @Test
//    public void atualizarDeveAlterarDados() {
//        ContaModel c = conta();
//        ClienteModel cli1 = new ClienteModel(2, "João", "49026915822", "Rua paulista",
//                "44556677");
//        this.contaRepository.save(c);
//
//        c.setAgencia("12345");
//        c.setNumero("4433");
//        c.setDigitoVerificador("1");
//        c.setTipo(TipoConta.JURIDICA);
//        c.setCliente(cli1);
//        c.setSaldo(100.0);
//        c.setSaques(250);
//
//        assertThat(c.getAgencia()).isEqualTo("12345");
//        assertThat(c.getNumero()).isEqualTo("4433");
//        assertThat(c.getDigitoVerificador()).isEqualTo("1");
//        assertThat(c.getTipo()).isEqualTo(TipoConta.JURIDICA);
//        assertThat(c.getCliente().getNome()).isEqualTo("João");
//        assertThat(c.getSaldo()).isEqualTo(100);
//        assertThat(c.getSaques()).isEqualTo(250);
//
//    }

    @Test
    public void depositarDeveSomarValor() {
        ContaModel conta = conta();
        TransacaoModel t1 = new TransacaoModel();
        t1.setValor(100);
        given(contaRepository.findByNumero(conta.getNumero())).willReturn(Optional.of(conta));

        contaService.depositar(conta.getNumero(), t1);
        verify(contaRepository).findByNumero(conta.getNumero());
    }

//    @Test
//    public void sacarDeveSubtrairValorETaxa() {
//        ContaModel conta = conta();
//        TransacaoModel t1 = new TransacaoModel();
//        t1.setValor(100);
//        given(contaRepository.findByNumero(conta.getNumero())).willReturn(Optional.of(conta));
//
//        contaService.sacar(conta.getNumero(), t1);
//        verify(contaRepository).findByNumero(conta.getNumero());
//    }

    @Test
    public void transferirDeveTransferirValor() {
        ContaModel conta = conta();
        ContaModel conta2 = conta2();
        TransacaoModel t1 = new TransacaoModel();
        t1.setValor(100);
        given(contaRepository.findByNumero(conta.getNumero())).willReturn(Optional.of(conta));
        given(contaRepository.findByNumero(conta2.getNumero())).willReturn(Optional.of(conta2));

        contaService.transferir(conta.getNumero(), conta2.getNumero(), t1);
        verify(contaRepository).findByNumero(conta.getNumero());
        verify(contaRepository).findByNumero(conta2.getNumero());
    }

//    @Test
//    public void extratoContaDeveRetornarExtrato(){
//        ContaModel conta = conta();
//
//        given(operacoesRepository.findAllByNumeroConta(conta.getNumero())).willReturn((List<OperacoesModel>) conta);
//
//        List<OperacoesModel> expected = contaService.extrato(conta.getNumero());
//
//        assertEquals(conta, expected);
//        verify(operacoesRepository).findAllByNumeroConta(conta.getNumero());
//    }

    @Test
    public void extratoTodasAsContas(){
        List<OperacoesModel> op = new ArrayList();

        op.add(new OperacoesModel());

        given(operacoesRepository.findAll()).willReturn(op);

        List<OperacoesModel> expected = contaService.exibirExtratos();

        assertEquals(op, expected);
        verify(operacoesRepository).findAll();
    }

}

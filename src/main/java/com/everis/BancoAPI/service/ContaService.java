package com.everis.BancoAPI.service;

import com.everis.BancoAPI.exceptions.*;
import com.everis.BancoAPI.kafka.KafkaProducerSaques;
import com.everis.BancoAPI.model.*;
import com.everis.BancoAPI.repository.ClienteRepository;
import com.everis.BancoAPI.repository.ContaRepository;
import com.everis.BancoAPI.repository.OperacoesRepository;
import org.json.simple.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Component
public class ContaService extends OperacoesService {

    @Autowired
    private ContaRepository repository;

    @Autowired
    private OperacoesRepository repositoryOP;

    @Autowired
    private ClienteRepository repositoryCli;

    @Autowired
    private ModelMapper modelMapper;

    public ContaModel consultar(String numero){
        ContaModel conta = repository.findByNumero(numero).orElseThrow(() ->
                new ContaNaoEncontrada("Conta com número " + numero + " não encontrada na base de dados."));
        return conta;
    }

    public List<ContaModel> listar() {
        List<ContaModel> contas = repository.findAll();
        if (contas.isEmpty()){
            throw new SemRegistros("Não há nenhum cliente cadastrado.");
        }
        return contas;
    }

    public ContaModel salvar(@Valid ContaModel conta) {

        Optional<ContaModel> c1 = repository.findByNumero(conta.getNumero());
        if (c1.isPresent()) {
            throw new ContaExistente("Já existe uma conta com o número " + conta.getNumero() + " registrada na base de dados.");
        }
        repositoryCli.findById(conta.getCliente().getCodigo()).orElseThrow(
                ()-> new ClienteNaoEncontrado("Cliente com código " + conta.getCliente().getCodigo() + " não encontrada na base de dados."));

        return repository.save(modelMapper.map(conta, ContaModel.class));
    }


    public ContaModel deletar(String numero) {
        ContaModel conta = repository.findByNumero(numero).orElseThrow(
                () -> new ClienteNaoEncontrado("Conta com número " + numero + " não encontrada na base de dados."));

        repository.deleteById(conta.getCodigo());
        return conta;
    }

    public ContaModel atualizar(Integer codigo, @Valid ContaModel conta) {

        ContaModel c1 = repository.findById(codigo).orElseThrow(
                () -> new ClienteNaoEncontrado("Conta com código " + codigo + " não encontradq na base de dados."));

            c1.setCodigo(codigo);
            c1.setAgencia(conta.getAgencia());
            c1.setNumero(conta.getNumero());
            c1.setDigitoVerificador(conta.getDigitoVerificador());
            c1.setTipo(conta.getTipo());
            c1.setCliente(conta.getCliente());
            c1.setSaldo(conta.getSaldo());
            c1.setSaques(conta.getSaques());
            return repository.save(c1);
    }

    public ContaModel depositar(String numero, TransacaoModel transacao) {
        ContaModel conta = repository.findByNumero(numero).orElseThrow(() ->
                new ContaNaoEncontrada("Conta com número " + numero + " não encontrada na base de dados."));

        checaValorNegativo(transacao);
        conta.setSaldo(conta.getSaldo() + transacao.getValor());
        repository.save(conta);
        salvarOperacao(conta, transacao.getValor(), TipoOperacao.DEPOSITO.getDesc());
        return conta;
    }

    public ContaModel sacar(String numero, TransacaoModel transacao) {
        ContaModel conta = repository.findByNumero(numero).orElseThrow(() ->
                new ContaNaoEncontrada("Conta com número " + numero + " não encontrada na base de dados."));

        checaSaldo(conta, transacao);
        checaValorNegativo(transacao);

        if (conta.getSaques() < conta.getTipo().getSaques()) {
            conta.setSaldo(conta.getSaldo() - transacao.getValor());
            salvarOperacao(conta, transacao.getValor(), TipoOperacao.SAQUE.getDesc());
        } else {
            conta.setSaldo(conta.getSaldo() - transacao.getValor() - conta.getTipo().getTaxa());
            salvarOperacao(conta, transacao.getValor(), TipoOperacao.SAQUE.getDesc(), conta.getTipo().getTaxa());
        }
        repository.save(conta);
        chamaKafka(numero);
        return conta;
    }

    public void chamaKafka(String numero){
        KafkaProducerSaques kafka = new KafkaProducerSaques();
        try {
            kafka.EnviarDadosClienteSaque(numero);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<ContaModel> transferir(String numero1, String numero2, TransacaoModel transacao){
        ContaModel conta1 = repository.findByNumero(numero1).orElseThrow(
                ()-> new ContaNaoEncontrada("Conta com número " + numero1 + " não encontrada na base de dados."));
        ContaModel conta2 = repository.findByNumero(numero2).orElseThrow(
                ()-> new ContaNaoEncontrada("Conta com número " + numero2 + " não encontrada na base de dados."));

        checaSaldo(conta1, transacao);
        checaValorNegativo(transacao);

        conta1.setSaldo(conta1.getSaldo() - transacao.getValor());
        conta2.setSaldo(conta2.getSaldo() + transacao.getValor());
        repository.save(conta1);
        repository.save(conta2);
        salvarOperacao(conta1, transacao.getValor(), TipoOperacao.TRANSFERENCIA_SAIDA.getDesc());
        salvarOperacao(conta2, transacao.getValor(), TipoOperacao.TRANSFERENCIA.getDesc());

        List<ContaModel> contas = new ArrayList<>();
        contas.add(conta1);
        contas.add(conta2);

        return contas;

    }

    public void checaSaldo(ContaModel conta, TransacaoModel transacao){
        if (conta.getSaldo() < transacao.getValor()){
            throw new NaoHaSaldo("Não há saldo suficiente para completar a ação");
        }
    }

    public void checaValorNegativo(TransacaoModel transacao){
        if(transacao.getValor() <= 0)
            throw new ValorNegativo("Não é possível realizar uma operação com valor menor ou igual a 0.");
    }

    public List<OperacoesModel> extrato(String numero) {
        List<OperacoesModel> op = repositoryOP.findAllByNumeroConta(numero);
        if (op.isEmpty()) {
            throw new SemRegistros("Não há nenhuma operação registrada para a conta de número " + numero + ".");
        }
        return op;
    }

    public List<OperacoesModel> exibirExtratos() {
        List<OperacoesModel> op =  repositoryOP.findAll();
        if (op.isEmpty()) {
            throw new SemRegistros("Não há nenhuma operação registrada.");
        }
        return op;
    }

}

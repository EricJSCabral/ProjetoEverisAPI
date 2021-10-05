package com.everis.BancoAPI.service;

import com.everis.BancoAPI.exceptions.*;
import com.everis.BancoAPI.kafka.KafkaProducerSaques;
import com.everis.BancoAPI.model.*;
import com.everis.BancoAPI.repository.ClienteRepository;
import com.everis.BancoAPI.repository.ContaRepository;
import com.everis.BancoAPI.repository.OperacoesRepository;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
        log.info("Operação consultar realizada com sucesso.");
        return conta;
    }

    public List<ContaModel> listar() {
        List<ContaModel> contas = repository.findAll();
        if (contas.isEmpty()){
            log.info("Operação listar não realizada.");
            throw new SemRegistros("Não há nenhum cliente cadastrado.");
        }
        log.info("Operação listar realizada com sucesso.");
        return contas;
    }

    public ContaModel salvar(@Valid ContaModel conta) {

        Optional<ContaModel> c1 = repository.findByNumero(conta.getNumero());
        if (c1.isPresent()) {
            log.info("Operação salvar não realizada.");
            throw new ContaExistente("Já existe uma conta com o número " + conta.getNumero() + " registrada na base de dados.");
        }
        repositoryCli.findById(conta.getCliente().getCodigo()).orElseThrow(
                ()-> new CodigoNaoEncontrado("Cliente com código " + conta.getCliente().getCodigo() + " não encontrada na base de dados."));

        log.info("Operação salvar realizada com sucesso.");
        return repository.save(modelMapper.map(conta, ContaModel.class));
    }


    public ContaModel deletar(String numero) {
        ContaModel conta = repository.findByNumero(numero).orElseThrow(
                () -> new ContaNaoEncontrada("Conta com número " + numero + " não encontrada na base de dados."));

        repository.deleteById(conta.getCodigo());
        log.info("Operação deletar realizada com sucesso.");
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
            log.info("Operação atualizar realizada com sucesso.");
            return repository.save(c1);
    }

    public ContaModel depositar(String numero, TransacaoModel transacao) {
        ContaModel conta = repository.findByNumero(numero).orElseThrow(() ->
                new ContaNaoEncontrada("Conta com número " + numero + " não encontrada na base de dados."));

        checaValorNegativo(transacao);
        conta.setSaldo(conta.getSaldo() + transacao.getValor());
        repository.save(conta);
        salvarOperacao(conta, transacao.getValor(), TipoOperacao.DEPOSITO.getDesc());
        log.info("Operação depositar realizada com sucesso.");
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
        log.info("Operação sacar realizada com sucesso.");
        repository.save(conta);
        chamaKafka(numero);

        ContaModel contaAtt = repository.findByNumero(numero).orElseThrow(() ->
                new ContaNaoEncontrada("Conta com número " + numero + " não encontrada na base de dados."));
        return contaAtt;
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
        log.info("Operação transferir realizada com sucesso.");
        salvarOperacao(conta1, transacao.getValor(), TipoOperacao.TRANSFERENCIA_SAIDA.getDesc());
        salvarOperacao(conta2, transacao.getValor(), TipoOperacao.TRANSFERENCIA.getDesc());

        List<ContaModel> contas = new ArrayList<>();
        contas.add(conta1);
        contas.add(conta2);

        return contas;

    }

    public void checaSaldo(ContaModel conta, TransacaoModel transacao){
        if (conta.getSaldo() < transacao.getValor()){
            log.info("Operação não realizada.");
            throw new NaoHaSaldo("Não há saldo suficiente para completar a ação");
        }
    }

    public void checaValorNegativo(TransacaoModel transacao){
        if(transacao.getValor() <= 0) {
            log.info("Operação não realizada.");
            throw new ValorNegativo("Não é possível realizar uma operação com valor menor ou igual a 0.");
        }
    }

    public List<OperacoesModel> extrato(String numero) {
        List<OperacoesModel> op = repositoryOP.findAllByNumeroConta(numero);
        if (op.isEmpty()) {
            log.info("Operação consultar extrato não realizada.");
            throw new SemRegistros("Não há nenhuma operação registrada para a conta de número " + numero + ".");
        }
        log.info("Operação consultar extrato realizada com sucesso.");
        return op;
    }

    public List<OperacoesModel> exibirExtratos() {
        List<OperacoesModel> op =  repositoryOP.findAll();
        if (op.isEmpty()) {
            log.info("Operação consultar todos os extratos não realizada.");
            throw new SemRegistros("Não há nenhuma operação registrada.");
        }
        log.info("Operação consultar todos os extratos realizada com sucesso.");
        return op;
    }

}

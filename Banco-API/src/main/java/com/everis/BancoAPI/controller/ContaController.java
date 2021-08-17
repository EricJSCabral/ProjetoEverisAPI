package com.everis.BancoAPI.controller;

import com.everis.BancoAPI.kafka.KafkaProducerSaques;
import com.everis.BancoAPI.model.*;
import com.everis.BancoAPI.repository.ContaRepository;
import com.everis.BancoAPI.repository.OperacoesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;


@RestController
public class ContaController extends OperacoesController {

    @Autowired
    private ContaRepository repository;

    @Autowired
    private OperacoesRepository repositoryOP;

//    @Autowired
//    KafkaTemplate<String, String> kafkaTemplate;
//
//    private static final String TOPIC = "NewTopic";
//
//    @GetMapping(path = "/teste/{mensagem}")
//    public String enviarMensagem(@PathVariable("mensagem") final String mensagem){
//        kafkaTemplate.send(TOPIC, mensagem);
//        return "Enviado com sucesso";
//    }
    int quantSaques = 0;
    TipoConta tipoConta;
    
    @GetMapping(path = "/api/conta/{codigo}")
    public ResponseEntity consultar(@PathVariable("codigo") Integer codigo) {
        return repository.findById(codigo)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(path = "/api/conta/listar")
    public ResponseEntity<List<ContaModel>> listarTudo() {
        List<ContaModel> contas = (List<ContaModel>) repository.findAll();
        return ResponseEntity.ok().body(contas);
    }

    @PostMapping(path = "/api/conta/salvar")
    public ContaModel salvar(@RequestBody ContaModel conta) {
        return repository.save(conta);
    }

    @DeleteMapping(path = "/api/conta/delete/{codigo}")
    public void deletar(@PathVariable("codigo") Integer codigo) {
        repository.deleteById(codigo);
    }

    @PutMapping(path = "/api/conta/atualizar")
    public ContaModel atualizar(@RequestBody ContaModel conta) {
        return repository.save(conta);
    }

    //Método para depositar
    @PutMapping(path = "/api/conta/depositar/{codigo}/")
    public ResponseEntity depositar(@PathVariable("codigo") Integer codigo,
                                    @RequestBody TransacaoModel transacao) {
        Optional<ContaModel> conta = repository.findById(codigo);
        return conta.map(busca -> {
            busca.setSaldo(busca.getSaldo() + transacao.getValor());
            ContaModel salvo = repository.save(busca);

            salvarOperacao(salvo,transacao.getValor(),TipoOperacao.DEPOSITO.getDesc());
            return ResponseEntity.ok().body(salvo);
        }).orElse(ResponseEntity.notFound().build());
    }

    //Método para sacar
    @PutMapping(path = "/api/conta/sacar/{codigo}/")
    public ResponseEntity sacar(@PathVariable("codigo") Integer codigo,
                                @RequestBody TransacaoModel transacao) {
        Optional<ContaModel> conta = repository.findById(codigo);
        System.out.println(ReconhecerTipoConta(codigo));
        return conta.map(busca -> {
            if (busca.getSaldo() > transacao.getValor()) {
                if (busca.getSaques() < quantSaques ) {
                    busca.setSaldo(busca.getSaldo() - transacao.getValor());
                    ContaModel salvo = repository.save(busca);
                    salvarOperacao(salvo, transacao.getValor(), TipoOperacao.SAQUE.getDesc());
                    KafkaProducerSaques kafka = new KafkaProducerSaques();
                    try {
                        kafka.EnviarDadosClienteSaque(codigo);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return ResponseEntity.ok().body(salvo);
                } else {
                    TaxaController t = new TaxaController();
                    int taxa = t.aplicarTaxa(conta);
                    busca.setSaldo(busca.getSaldo() - transacao.getValor() - taxa);
                    ContaModel salvo = repository.save(busca);
                    salvarOperacao(salvo, transacao.getValor(), TipoOperacao.SAQUE.getDesc(), taxa);
                    return ResponseEntity.ok().body(salvo);
                }
            } else {
                return ResponseEntity.badRequest().body("Erro. Não há saldo suficiente " +
                        "para realizar a operação");
            }
        }).orElse(ResponseEntity.notFound().build());
    }

    private String ReconhecerTipoConta(Integer id) {
        Optional<ContaModel> conta = repository.findById(id);
        conta.map(map -> {
            tipoConta = map.getTipo();
            quantSaques = tipoConta.getSaques();
            return tipoConta;
        });
        return tipoConta.getDescricao();
    }

    //Método para transferências
    @PutMapping(path = "/api/conta/transferir/{codigo}/{codigo2}/")
    public ResponseEntity transferir(@PathVariable("codigo") Integer codigo,
                                     @PathVariable ("codigo2") Integer codigo2,
                                     @RequestBody TransacaoModel transacao){
        Optional<ContaModel> conta1 = repository.findById(codigo);
        Optional<ContaModel> conta2 = repository.findById(codigo2);

                return conta1.map(busca -> {
                if (busca.getSaldo() > transacao.getValor()) {
                busca.setSaldo(busca.getSaldo() - transacao.getValor());
                ContaModel salvo = repository.save(busca);

                    conta2.map(busca2 -> {
                        busca2.setSaldo(busca2.getSaldo() + transacao.getValor());
                        ContaModel salvo2 = repository.save(busca2);
                        salvarOperacao(salvo, salvo2, transacao.getValor());
                        return ResponseEntity.ok().body(salvo2);
                    });
                    return ResponseEntity.ok().body(salvo);
            } else {
                return ResponseEntity.badRequest().body("Erro. Não há saldo suficiente " +
                        "para realizar a operação");
            }
                }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(path = "/api/conta/extrato/{codigo}")
    public ResponseEntity Extrato(@PathVariable("codigo") Integer codigo) {
        List<OperacoesModel> op = (List<OperacoesModel>) repositoryOP.findAll();
        List<OperacoesModel> op2 = null;

        return ResponseEntity.ok().body(op.stream().map(busca ->
        { 
            if (busca.getId_conta1() == codigo || busca.getId_conta2() == codigo) {
               op2.add(busca);
            }
            return ResponseEntity.ok().body(op2);
        } ));
    }

    //Método para listar todas as operações realizadas
    @GetMapping(path = "/api/conta/extrato/all")
    public ResponseEntity<List<OperacoesModel>> exibirExtratos() {
        List<OperacoesModel> op = (List<OperacoesModel>) repositoryOP.findAll();
        return ResponseEntity.ok().body(op);
    }

}


package com.everis.BancoAPI.controller;

import com.everis.BancoAPI.kafka.KafkaProducerSaques;
import com.everis.BancoAPI.model.*;
import com.everis.BancoAPI.repository.ClienteRepository;
import com.everis.BancoAPI.repository.ContaRepository;
import com.everis.BancoAPI.repository.OperacoesRepository;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;


@RestController
public class ContaController extends OperacoesController {

    @Autowired
    private ContaRepository repository;

    @Autowired
    private OperacoesRepository repositoryOP;

    @Autowired
    private ClienteRepository repositoryCli;

    int quantSaques = 0;
    TipoConta tipoConta;
    
    @GetMapping(path = "/api/conta")
    public HttpEntity<?> consultar(@RequestParam("numero") String numero) {
        Optional<ContaModel> conta = repository.findByNumero(numero);
        if(conta.isPresent()) {
            return repository.findByNumero(numero)
                    .map(record -> ResponseEntity.ok().body(record))
                    .orElse(ResponseEntity.notFound().build());
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mensagem:", "Conta não encontrado na base de dados");
            return ResponseEntity.status(404).body(jsonObject);
        }
    }

    @GetMapping(path = "/api/conta/listar")
    public ResponseEntity<List<?>> listarTudo() {
        List<ContaModel> contas = (List<ContaModel>) repository.findAll();
        if (contas.isEmpty()){
            List<String> json = Collections.singletonList("Não há nenhum registro na base de dados.");
            return ResponseEntity.status(200).body(json);
        }
        return ResponseEntity.ok().body(contas);
    }

    @PostMapping(path = "/api/conta/salvar")
    public ResponseEntity<?> salvar(@RequestBody @Valid ContaModel conta) {
        Optional <ContaModel> c1 = repository.findByNumero(conta.getNumero());
        Optional <ClienteModel> c2 = repositoryCli.findById(conta.getCliente().getCodigo());
        //if(c1.get().getTipo().equals(TipoConta.FISICA) || c1.get().getTipo().equals(TipoConta.JURIDICA) ||
          //      c1.get().getTipo().equals(TipoConta.GOVERNAMENTAL) ) {
            if (c2.isPresent()) {
                if (c1.isPresent()) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("mensagem:", "Já existe uma contGa com esse número na base de dados.");
                    return ResponseEntity.status(400).body(jsonObject);
                } else {
                    repository.save(conta);
                    return ResponseEntity.ok().body(conta);
                }
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("mensagem:", "Não existe um cliente com código" + conta.getCliente().getCodigo() + " na base de dados.");
                return ResponseEntity.status(400).body(jsonObject);
            }
//        } else {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("mensagem:", "O tipo da conta deve ser apenas FISICA, JURIDICA ou GOVERNAMENTAL");
//            return ResponseEntity.status(400).body(jsonObject);
//        }
    }


    @DeleteMapping(path = "/api/conta/delete")
    public ResponseEntity deletar(@RequestParam("numero") String numero) {
        Optional<ContaModel> conta = repository.findByNumero(numero);
        JSONObject jsonObject = new JSONObject();
        if (conta.isPresent()){
            jsonObject.put("mensagem:", "Conta deletada com sucesso.");
            repository.deleteById(conta.get().getCodigo());
            return ResponseEntity.status(202).body(jsonObject);
        } else {
            jsonObject.put("mensagem:", "Conta não encontrada na base de dados.");
            return ResponseEntity.status(404).body(jsonObject);
        }
    }

    @PutMapping(path = "/api/conta/atualizar")
    public ResponseEntity atualizar(@RequestParam ("codigo") Integer codigo, @RequestBody @Valid ContaModel conta) {
        Optional<ContaModel> contas = repository.findById(codigo);
        if (contas.isPresent()) {
            ContaModel c1 = new ContaModel();
            c1.setCodigo(codigo);
            c1.setAgencia(conta.getAgencia());
            c1.setNumero(conta.getNumero());
            c1.setDigitoVerificador(conta.getDigitoVerificador());
            c1.setTipo(conta.getTipo());
            c1.setCliente(conta.getCliente());
            c1.setSaldo(conta.getSaldo());
            c1.setSaques(conta.getSaques());
            repository.save(c1);
            return ResponseEntity.ok().body(c1);
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mensagem:", "Conta não encontrada na base de dados");
            return ResponseEntity.status(404).body(jsonObject);
        }
    }

    //Método para depositar
    @PutMapping(path = "/api/conta/depositar")
    public ResponseEntity depositar(@RequestParam("numero") String numero,
                                    @RequestBody TransacaoModel transacao) {
        Optional<ContaModel> conta = repository.findByNumero(numero);

        if (conta.isPresent()) {
            return conta.map(busca -> {
                busca.setSaldo(busca.getSaldo() + transacao.getValor());
                ContaModel salvo = repository.save(busca);

                salvarOperacao(salvo, transacao.getValor(), TipoOperacao.DEPOSITO.getDesc());
                return ResponseEntity.ok().body(salvo);
            }).orElse(ResponseEntity.notFound().build());
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mensagem:", "Conta não encontrada na base de dados.");
            return ResponseEntity.status(404).body(jsonObject);
        }
    }

    //Método para sacar
    @PutMapping(path = "/api/conta/sacar")
    public ResponseEntity sacar(@RequestParam("numero") String numero,
                                @RequestBody TransacaoModel transacao) {
        Optional<ContaModel> conta = repository.findByNumero(numero);
        if (conta.isPresent()) {
            //System.out.println(reconhecerTipoConta(numero));
            return conta.map(busca -> {
                if (busca.getSaldo() > transacao.getValor()) {
                    if (busca.getSaques() < busca.getTipo().getSaques()) {
                        busca.setSaldo(busca.getSaldo() - transacao.getValor());
                        ContaModel salvo = repository.save(busca);
                        salvarOperacao(salvo, transacao.getValor(), TipoOperacao.SAQUE.getDesc());
                        KafkaProducerSaques kafka = new KafkaProducerSaques();
                        try {
                            kafka.EnviarDadosClienteSaque(numero);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return ResponseEntity.ok().body(repository.findByNumero(numero));
                    } else {
                        TaxaController t = new TaxaController();
                        int taxa = t.aplicarTaxa(conta);
                        busca.setSaldo(busca.getSaldo() - transacao.getValor() - taxa);
                        ContaModel salvo = repository.save(busca);
                        salvarOperacao(salvo, transacao.getValor(), TipoOperacao.SAQUE.getDesc(), taxa);
                        KafkaProducerSaques kafka = new KafkaProducerSaques();
                        try {
                            kafka.EnviarDadosClienteSaque(numero);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return ResponseEntity.ok().body(repository.findByNumero(numero));
                    }
                } else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("mensagem:", "Erro. Não há saldo suficiente para realizar a operação.");
                    return ResponseEntity.badRequest().body(jsonObject);
                }
            }).orElse(ResponseEntity.notFound().build());
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mensagem:", "Conta não encontrada na base de dados");
            return ResponseEntity.status(404).body(jsonObject);
        }
    }

    //Método para transferências
    @PutMapping(path = "/api/conta/transferir")
    public ResponseEntity transferir(@RequestParam("numero1") String numero1,
                                        @RequestParam ("numero2") String numero2,
                                        @RequestBody TransacaoModel transacao) {
        Optional<ContaModel> conta1 = repository.findByNumero(numero1);
        Optional<ContaModel> conta2 = repository.findByNumero(numero2);

        if (conta1.isPresent() && conta2.isPresent()) {
                return conta1.map(busca -> {
                    if (busca.getSaldo() > transacao.getValor()) {
                        busca.setSaldo(busca.getSaldo() - transacao.getValor());
                        ContaModel salvo = repository.save(busca);

                        conta2.map(busca2 -> {
                            busca2.setSaldo(busca2.getSaldo() + transacao.getValor());
                            ContaModel salvo2 = repository.save(busca2);
                            salvarOperacao(salvo, transacao.getValor(), TipoOperacao.TRANSFERENCIA_SAIDA.getDesc());
                            salvarOperacao(salvo2, transacao.getValor(), TipoOperacao.TRANSFERENCIA.getDesc());
                            return ResponseEntity.ok().body(salvo2);
                        });
                        return ResponseEntity.ok().body(salvo);
                    } else {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("mensagem:", "Erro. Não há saldo suficiente para realizar a operação.");
                        return ResponseEntity.badRequest().body(jsonObject);
                    }
                }).orElse(ResponseEntity.notFound().build());
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mensagem:", "Erro. As contas não foram informadas corretamente.");
            return ResponseEntity.badRequest().body(jsonObject);
        }
    }

    @GetMapping(path = "/api/conta/extrato")
    public ResponseEntity<List<?>> Extrato(@RequestParam("numero") String numero) {

        List<OperacoesModel> op = repositoryOP.findAllByNumeroConta(numero);

        if (op.isEmpty()){
            List<String> json = Collections.singletonList("Não há nenhuma operação desta conta registrada na base de dados.");
            return ResponseEntity.status(400).body(json);
        } else {
            return ResponseEntity.ok().body(op);
        }
    }

    //Método para listar todas as operações realizadas
    @GetMapping(path = "/api/conta/extrato/all")
    public ResponseEntity<List<?>> exibirExtratos() {
        List<OperacoesModel> op = (List<OperacoesModel>) repositoryOP.findAll();
        if (op.isEmpty()) {
            List<String> json = Collections.singletonList("Não há nenhum registro na base de dados.");
            return ResponseEntity.status(404).body(json);
        } else {
            return ResponseEntity.ok().body(op);
        }
    }

}


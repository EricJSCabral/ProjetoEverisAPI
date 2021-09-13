package com.everis.BancoAPI.service;

import com.everis.BancoAPI.model.ClienteModel;
import com.everis.BancoAPI.repository.ClienteRepository;
import com.everis.BancoAPI.repository.ContaRepository;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    @Autowired
    private ContaRepository contaRepository;

    public ResponseEntity consultar(Integer codigo){
        Optional<ClienteModel> cliente = repository.findById(codigo);
        if (cliente.isPresent()) {
            return cliente.map(record -> ResponseEntity.ok().body(record))
                    .orElse(ResponseEntity.notFound().build());
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mensagem:", "Cliente não encontrado na base de dados");
            return ResponseEntity.status(404).body(jsonObject);
        }
    }

    public ResponseEntity<List<?>> listar(){
        List<ClienteModel> clientes = repository.findAll();
        if(clientes.isEmpty()){
            List<String> mensagem = Collections.singletonList("Não há nenhum cliente cadastrado");
            return ResponseEntity.status(404).body(mensagem);
        } else {
            return ResponseEntity.ok().body(clientes);
        }
    }

    public ResponseEntity<?> salvar(@Valid ClienteModel cliente){
        Optional <ClienteModel> c1 = repository.findByCpf(cliente.getCpf());
        if (c1.isPresent()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mensagem:", "Este CPF já está registrado na base de dados.");
            jsonObject.put("conta:", c1);
            return ResponseEntity.status(400).body(jsonObject);
        } else {
            repository.save(cliente);
            return ResponseEntity.ok().body(cliente);
        }
    }

    public ResponseEntity deletar(Integer codigo){
        Optional<ClienteModel> cliente = repository.findById(codigo);
//        Optional<ContaModel> contas = contaRepository.findAllByCliente(codigo);
        if (cliente.isPresent()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mensagem:", "Cliente deletado com sucesso.");
            jsonObject.put("cliente:", cliente);
            repository.deleteById(codigo);
            return ResponseEntity.status(202).body(jsonObject);
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mensagem:", "Cliente não encontrado na base de dados");
            return ResponseEntity.status(404).body(jsonObject);
        }
    }

    public ResponseEntity atualizar(Integer codigo, @Valid ClienteModel cliente){
        Optional<ClienteModel> clientes = repository.findById(codigo);
        if (clientes.isPresent()) {
            ClienteModel c1 = new ClienteModel();
            c1.setCodigo(codigo);
            c1.setNome(cliente.getNome());
            c1.setCpf(cliente.getCpf());
            c1.setEndereco(cliente.getEndereco());
            c1.setTelefone(cliente.getTelefone());
            repository.save(c1);
            return ResponseEntity.ok().body(c1);
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mensagem:", "Cliente não encontrado na base de dados");
            return ResponseEntity.status(404).body(jsonObject);
        }
    }
}

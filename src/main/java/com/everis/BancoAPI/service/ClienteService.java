package com.everis.BancoAPI.service;

import com.everis.BancoAPI.exceptions.*;
import com.everis.BancoAPI.model.ClienteModel;
import com.everis.BancoAPI.model.ContaModel;
import com.everis.BancoAPI.repository.ClienteRepository;
import com.everis.BancoAPI.repository.ContaRepository;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ClienteService(ClienteRepository repository){}

    public ClienteModel consultar(String cpf) {
        ClienteModel cliente = repository.findByCpf(cpf).orElseThrow(() ->
                new ClienteNaoEncontrado("Cliente com CPF " + cpf + " não encontrado na base de dados."));
        log.info("Operação consultar realizada com sucesso.");
        return cliente;
    }

    public List<ClienteModel> listar(){
        List<ClienteModel> clientes = repository.findAll();
        if (clientes.isEmpty()){
            log.info("Operação listar não realizada.");
            throw new SemRegistros("Não há nenhum cliente cadastrado.");
        }
        log.info("Operação listar realizada com sucesso.");
        return clientes;
    }

    public ClienteModel salvar(@Valid ClienteModel cliente){
        Optional<ClienteModel> c1 = repository.findByCpf(cliente.getCpf());
        if (c1.isPresent()){
            log.info("Operação salvar não realizada.");
            throw new ClienteExistente("Já existe um cliente com o CPF " + cliente.getCpf() + " registrado na base de dados.");
        }
        log.info("Operação salvar realizada com sucesso.");
        return repository.save(modelMapper.map(cliente, ClienteModel.class));
    }


    public ClienteModel deletar(String cpf){
       ClienteModel cliente = repository.findByCpf(cpf).orElseThrow(
               () -> new ClienteNaoEncontrado("Cliente com CPF " + cpf + " não encontrado na base de dados."));

       checaConta(cliente);
       log.info("Operação deletar realizada com sucesso.");
       repository.deleteById(cliente.getCodigo());
       return cliente;
    }

    public void checaConta(ClienteModel cliente){
        List<ContaModel> contas = contaRepository.findAllByCliente(cliente);
        if (!contas.isEmpty()){
            log.info("Operação deletar não realizada.");
            throw new ContaAtrelada("Não é possivel deletar este usuário, pois há contas registradas a este ID.");
        }
    }

    public ClienteModel atualizar(Integer codigo, @Valid ClienteModel cliente){
        ClienteModel c1 = repository.findById(codigo).orElseThrow(
                () -> new CodigoNaoEncontrado("Cliente com código " + codigo + " não encontrado na base de dados."));

            c1.setCodigo(codigo);
            c1.setNome(cliente.getNome());
            c1.setCpf(cliente.getCpf());
            c1.setEndereco(cliente.getEndereco());
            c1.setTelefone(cliente.getTelefone());
            log.info("Operação atualizar realizada com sucesso.");
            repository.save(c1);
            return c1;
    }
}

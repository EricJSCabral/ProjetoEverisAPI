package com.everis.BancoAPI.service;

import com.everis.BancoAPI.dto.request.ClienteRequest;
import com.everis.BancoAPI.exceptions.ClienteExistente;
import com.everis.BancoAPI.exceptions.ClienteNaoEncontrado;
import com.everis.BancoAPI.exceptions.SemRegistros;
import com.everis.BancoAPI.model.ClienteModel;
import com.everis.BancoAPI.repository.ClienteRepository;
import com.everis.BancoAPI.repository.ContaRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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
        return cliente;
    }

    public List<ClienteModel> listar(){
        List<ClienteModel> clientes = repository.findAll();
        if (clientes.isEmpty()){
            throw new SemRegistros("Não há nenhum cliente cadastrado.");
        }
            return clientes;
    }

    public ClienteModel salvar(@Valid ClienteModel cliente){
        Optional<ClienteModel> c1 = repository.findByCpf(cliente.getCpf());
        if (c1.isPresent()){
            throw new ClienteExistente("Já existe um cliente com o CPF " + cliente.getCpf() + " registrado na base de dados.");
        }
        return repository.save(modelMapper.map(cliente, ClienteModel.class));
    }

    public ClienteModel deletar(String cpf){
       ClienteModel cliente = repository.findByCpf(cpf).orElseThrow(
               () -> new ClienteNaoEncontrado("Cliente com CPF " + cpf + " não encontrado na base de dados."));

       repository.deleteById(cliente.getCodigo());
       return cliente;
    }

    public ClienteModel atualizar(Integer codigo, @Valid ClienteModel cliente){
        ClienteModel c1 = repository.findById(codigo).orElseThrow(
                () -> new ClienteNaoEncontrado("Cliente com código " + codigo + " não encontrado na base de dados."));

            c1.setCodigo(codigo);
            c1.setNome(cliente.getNome());
            c1.setCpf(cliente.getCpf());
            c1.setEndereco(cliente.getEndereco());
            c1.setTelefone(cliente.getTelefone());
            repository.save(c1);
            return c1;
    }
}

package com.everis.BancoAPI.controller;

import com.everis.BancoAPI.dto.request.ClienteRequest;
import com.everis.BancoAPI.dto.response.ClienteDeleteResponse;
import com.everis.BancoAPI.dto.response.ClienteResponse;
import com.everis.BancoAPI.model.ClienteModel;
import com.everis.BancoAPI.service.ClienteService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(path = "/api/cliente")
    public ClienteResponse consultar(@RequestParam("cpf") String cpf){
        ClienteModel cliente = clienteService.consultar(cpf);
        return modelMapper.map(cliente, ClienteResponse.class);
    }

    @GetMapping(path = "/api/cliente/listar")
    public List<ClienteResponse> listarTudo(){
        List<ClienteModel> cliente = clienteService.listar();
        return cliente.stream().map(this::toClienteResponse).collect(Collectors.toList());
    }

    @PostMapping(path = "/api/cliente/salvar")
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse salvar(@RequestBody @Valid ClienteRequest clienteRequest){
        ClienteModel c1 = clienteService.salvar(modelMapper.map(clienteRequest, ClienteModel.class));
        return modelMapper.map(c1, ClienteResponse.class);
    }


    @DeleteMapping(path = "/api/cliente/delete")
    @ResponseStatus(HttpStatus.OK)
    public ClienteDeleteResponse deletar (@RequestParam("cpf") String cpf){
       ClienteModel cliente = clienteService.deletar(cpf);
       return new ClienteDeleteResponse(cliente);
    }

    @PutMapping(path = "/api/cliente/atualizar")
    public ClienteResponse atualizar(@RequestParam("codigo") Integer codigo,
                                     @RequestBody @Valid ClienteRequest clienteRequest){

      ClienteModel c1 = clienteService.atualizar(codigo, modelMapper.map(clienteRequest, ClienteModel.class));
      return modelMapper.map(c1, ClienteResponse.class);
    }

    public ClienteResponse toClienteResponse(ClienteModel cliente){
        return modelMapper.map(cliente, ClienteResponse.class);
    }
}

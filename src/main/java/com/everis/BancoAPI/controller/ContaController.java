package com.everis.BancoAPI.controller;

import com.everis.BancoAPI.dto.request.ContaRequest;
import com.everis.BancoAPI.dto.response.*;
import com.everis.BancoAPI.model.ClienteModel;
import com.everis.BancoAPI.model.ContaModel;
import com.everis.BancoAPI.model.OperacoesModel;
import com.everis.BancoAPI.model.TransacaoModel;
import com.everis.BancoAPI.service.ContaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RestController
public class ContaController {

    @Autowired
    private ContaService contaService;

    @Autowired
    private ModelMapper modelMapper;
    
    @GetMapping(path = "/api/conta")
    public ContaResponse consultar(@RequestParam("numero") String numero) {
        ContaModel conta = contaService.consultar(numero);
        return modelMapper.map(conta, ContaResponse.class);
    }

    @GetMapping(path = "/api/conta/listar")
    public List<ContaResponse> listarTudo() {
        List<ContaModel> conta = contaService.listar();
        return conta.stream().map(this::toContaResponse).collect(Collectors.toList());
    }

    @PostMapping(path = "/api/conta/salvar")
    @ResponseStatus(HttpStatus.CREATED)
    public ContaResponse salvar(@RequestBody @Valid ContaRequest conta) {
        ContaModel c1 = contaService.salvar(modelMapper.map(conta, ContaModel.class));
        return modelMapper.map(c1, ContaResponse.class);
    }


    @DeleteMapping(path = "/api/conta/delete")
    @ResponseStatus(HttpStatus.OK)
    public ContaDeleteResponse deletar(@RequestParam("numero") String numero) {
        ContaModel conta = contaService.deletar(numero);
        return new ContaDeleteResponse(conta);
    }

    @PutMapping(path = "/api/conta/atualizar")
    public ContaResponse atualizar(@RequestParam ("codigo") Integer codigo, @RequestBody @Valid ContaRequest conta) {
        ContaModel c1 = contaService.atualizar(codigo, modelMapper.map(conta, ContaModel.class));
        return modelMapper.map(c1, ContaResponse.class);
    }

    //Método para depositar
    @PutMapping(path = "/api/conta/depositar")
    public ContaResponse depositar(@RequestParam("numero") String numero,
                                    @RequestBody TransacaoModel transacao) {
       ContaModel c1 = contaService.depositar(numero, transacao);
       return modelMapper.map(c1, ContaResponse.class);
    }

    //Método para sacar
    @PutMapping(path = "/api/conta/sacar")
    public ContaResponse sacar(@RequestParam("numero") String numero,
                                @RequestBody TransacaoModel transacao) {
         ContaModel c1 = contaService.sacar(numero, transacao);
         return modelMapper.map(c1, ContaResponse.class);
    }

    //Método para transferências
    @PutMapping(path = "/api/conta/transferir")
    public List<ContaResponse> transferir(@RequestParam("numero1") String numero1,
                                        @RequestParam ("numero2") String numero2,
                                        @RequestBody TransacaoModel transacao) {
       List<ContaModel> c1 = contaService.transferir(numero1, numero2, transacao);
       return c1.stream().map(this::toContaResponse).collect(Collectors.toList());
    }

    @GetMapping(path = "/api/conta/extrato")
    public List<OperacoesResponse> extrato(@RequestParam("numero") String numero) {
        List<OperacoesModel> op = contaService.extrato(numero);
        return op.stream().map(this::toOperacoesResponse).collect(Collectors.toList());
    }

    //Método para listar todas as operações realizadas
    @GetMapping(path = "/api/conta/extrato/all")
    public List<OperacoesResponse> exibirExtratos() {
        List<OperacoesModel> op =  contaService.exibirExtratos();
        return op.stream().map(this::toOperacoesResponse).collect(Collectors.toList());
    }

    public ContaResponse toContaResponse(ContaModel conta){
        return modelMapper.map(conta, ContaResponse.class);
    }

    public OperacoesResponse toOperacoesResponse(OperacoesModel operacoes){
        return modelMapper.map(operacoes, OperacoesResponse.class);
    }
}


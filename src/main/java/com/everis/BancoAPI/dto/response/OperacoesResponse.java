package com.everis.BancoAPI.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperacoesResponse {

    private int cod_operacao;
    private String numeroConta;
    private String tipo;
    private double valor;
    private int taxa;
    private String data;
}

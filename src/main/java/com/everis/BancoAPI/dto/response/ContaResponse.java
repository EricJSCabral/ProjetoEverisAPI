package com.everis.BancoAPI.dto.response;

import com.everis.BancoAPI.dto.request.ClienteRequest;
import com.everis.BancoAPI.model.ClienteModel;
import com.everis.BancoAPI.model.TipoConta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContaResponse {

    private Integer codigo;
    private String agencia;
    private String numero;
    private String digitoVerificador;
    private TipoConta tipo;
    private ClienteModel cliente;
    private double saldo;
    private int saques;
}

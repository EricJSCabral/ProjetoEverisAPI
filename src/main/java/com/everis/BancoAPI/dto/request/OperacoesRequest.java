package com.everis.BancoAPI.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperacoesRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cod_operacao;

    @Column(nullable = false)
    private String numeroConta;

    @Column (nullable = false)
    private String tipo;

    @Column (nullable = false)
    private double valor;

    @Column (nullable = true)
    private int taxa;

    @Column (nullable = false)
    private String data;
}

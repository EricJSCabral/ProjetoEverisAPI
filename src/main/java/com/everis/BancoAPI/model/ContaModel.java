package com.everis.BancoAPI.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.*;


@Entity(name = "contas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer codigo;

    private String agencia;

    private String numero;

    private String digitoVerificador;

    private TipoConta tipo;

    @ManyToOne
    @JoinColumn(name = "clientes_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ClienteModel cliente;


    private double saldo;

    private int saques;

    public ContaModel(String agencia, String numero, String digitoVerificador,
                      TipoConta tipo, ClienteModel cliente, double saldo, int saques) {
        this.agencia = agencia;
        this.numero = numero;
        this.digitoVerificador = digitoVerificador;
        this.tipo = tipo;
        this.cliente = cliente;
        this.saldo = saldo;
        this.saques = saques;
    }
}

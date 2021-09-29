package com.everis.BancoAPI.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity(name = "operacoes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperacoesModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cod_operacao;

    @Column (nullable = false)
    private String numeroConta;

    @Column (nullable = false)
    private String tipo;

    @Column (nullable = false)
    private double valor;

    @Column (nullable = true)
    private int taxa;

    @Column (nullable = false)
    private String data;

    public OperacoesModel(String numero, double valor, String tipo) {
        this.numeroConta = numero;
        this.valor = valor;
        this.tipo = tipo;
        this.taxa = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime dt = LocalDateTime.now();
        this.data = dt.format(formatter);
    }

    public OperacoesModel(String numero, double valor, String tipo, int taxa) {
        this.numeroConta = numero;
        this.valor = valor;
        this.tipo = tipo;
        this.taxa = taxa;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime dt = LocalDateTime.now();
        this.data = dt.format(formatter);
    }
}

package com.everis.BancoAPI.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity(name = "operacoes")
public class OperacoesModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cod_operacao;

    @Column (nullable = false)
    private int id_conta1;

    @Column (nullable = true)
    private int id_conta2;

    @Column (nullable = false)
    private String tipo;

    @Column (nullable = false)
    private double valor;

    @Column (nullable = true)
    private int taxa;

    @Column (nullable = false)
    private String data;

    public OperacoesModel(Integer codigo, double valor, String tipo) {
        this.id_conta1 = codigo;
        this.valor = valor;
        this.tipo = tipo;
        this.taxa = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime dt = LocalDateTime.now();
        this.data = dt.format(formatter);
    }

    public OperacoesModel(Integer codigo, double valor, String tipo, int taxa) {
        this.id_conta1 = codigo;
        this.valor = valor;
        this.tipo = tipo;
        this.taxa = taxa;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime dt = LocalDateTime.now();
        this.data = dt.format(formatter);
    }

    public OperacoesModel(Integer codigo, Integer cod2, double valor) {
        this.id_conta1 = codigo;
        this.id_conta2 = cod2;
        this.valor = valor;
        this.tipo = TipoOperacao.TRANSFERENCIA.getDesc();
        this.taxa = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime dt = LocalDateTime.now();
        this.data = dt.format(formatter);
    }

    public OperacoesModel() {
    }


    public int getCod_operacao() {
        return cod_operacao;
    }

    public void setCod_operacao(int cod_operacao) {
        this.cod_operacao = cod_operacao;
    }

    public int getId_conta1() {
        return id_conta1;
    }

    public void setId_conta1(int id_conta1) {
        this.id_conta1 = id_conta1;
    }

    public int getId_conta2() {
        return id_conta2;
    }

    public void setId_conta2(Integer id_conta2) {
        this.id_conta2 = id_conta2;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor; }

    public int getTaxa() {
        return taxa;
    }

    public void setTaxa(int taxa) {
        this.taxa = taxa;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

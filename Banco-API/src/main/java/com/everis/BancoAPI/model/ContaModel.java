package com.everis.BancoAPI.model;

import javax.persistence.*;

@Entity(name = "contas")
public class ContaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer codigo;

    @Column(nullable = false)
    private int agencia;

    @Column(nullable = false, unique = true)
    private int numero;

    @Column(nullable = false)
    private int digitoVerificador;

    @Column(nullable = false)
    private TipoConta tipo;

    @ManyToOne
    @JoinColumn(name = "clientes_id")
    private ClienteModel cliente;

    @Column(nullable = false)
    private double saldo;

    @Column(nullable = false)
    private int saques;

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public int getAgencia() {
        return agencia;
    }

    public void setAgencia(int agencia) {
        this.agencia = agencia;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getDigitoVerificador() {
        return digitoVerificador;
    }

    public void setDigitoVerificador(int digitoVerificador) {
        this.digitoVerificador = digitoVerificador;
    }

    public TipoConta getTipo() {
        return tipo;
    }

    public void setTipo(TipoConta tipo) {
        this.tipo = tipo;
    }

    public ClienteModel getCliente() {
        return cliente;
    }

    public void setCliente(ClienteModel cliente) {
        this.cliente = cliente;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public int getSaques() {
        return saques;
    }

    public void setSaques(int saques) {
        this.saques = saques;
    }

    public ContaModel() {
    }

    public ContaModel(Integer codigo, int agencia, int numero, int digitoVerificador,
                      TipoConta tipo, ClienteModel cliente, double saldo, int saques) {
        this.codigo = codigo;
        this.agencia = agencia;
        this.numero = numero;
        this.digitoVerificador = digitoVerificador;
        this.tipo = tipo;
        this.cliente = cliente;
        this.saldo = saldo;
        this.saques = saques;
    }
}

package com.everis.BancoAPI.model;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity(name = "contas")
public class ContaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer codigo;

    @Column
    @NotNull(message = "Campo não pode estar vazio")
    @Size(min=5, max=5, message = "Deve conter apenas 5 digitos")
    @Pattern(regexp = "[0-9]+", message = "Formato inválido. Apenas números são aceitos.")
    private String agencia;

    @Column
    @NotNull(message = "Campo não pode estar vazio")
    @Size(min=4, max=4, message = "Deve conter apenas 4 digitos")
    @Pattern(regexp = "[0-9]+", message = "Formato inválido. Apenas números são aceitos.")
    private String numero;

    @Column
    @NotNull(message = "Campo não pode estar vazio")
    @Size(min=1, max=1, message = "Deve conter apenas 1 digitos")
    @Pattern(regexp = "[0-9]+", message = "Formato inválido. Apenas números são aceitos.")
    private String digitoVerificador;

    @Column
    @NotNull(message = "O campo não pode estar vazio")
    @Enumerated(EnumType.ORDINAL)
    private TipoConta tipo;

    @ManyToOne
    @JoinColumn(name = "clientes_id")
    private ClienteModel cliente;

    @Column
    @NotNull
    private double saldo;

    @Column
    @NotNull
    private int saques;

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getDigitoVerificador() {
        return digitoVerificador;
    }

    public void setDigitoVerificador(String digitoVerificador) {
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

    public ContaModel(Integer codigo, String agencia, String numero, String digitoVerificador,
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

package com.everis.apibancoii.model;

import javax.persistence.*;

@Entity(name = "clientes")
public class ClienteModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer codigo;

    @Column(nullable = false, length = 50)
    public String nome;

    @Column(nullable = false, length = 14, unique = true)
    public String cpf;

    @Column(nullable = false, length = 50)
    public String endereco;

    @Column(nullable = false, length = 11)
    public String telefone;

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        //if (isCPF(cpf) || isCNPJ(cpf)) {
        this.cpf = cpf;
        //}
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public ClienteModel(Integer codigo, String nome, String cpf, String endereco, String telefone) {
        this.codigo = codigo;
        this.nome = nome;
        this.setCpf(cpf);
        this.endereco = endereco;
        this.telefone = telefone;
    }

    public ClienteModel() {
    }

}

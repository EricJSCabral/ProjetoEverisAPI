package com.everis.BancoAPI.model;


import org.hibernate.validator.constraints.UniqueElements;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
//import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.*;

@Entity(name = "clientes")
public class ClienteModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer codigo;

    @Column
    @NotNull(message = "Campo não pode estar vazio")
    @Size(min=5, max=50, message = "O tamanho é inválido")
    public String nome;

    @Column
    //@UniqueElements(message = "CPF já cadastrado na base de dados.")
    @CPF(message = "CPF inválido")
    //@CNPJ(message = "CNPJ inválido")
    public String cpf;

    @Column
    @NotNull(message = "Campo não pode estar vazio")
    @Size(min=10, max=50, message = "O tamanho é inválido")
    @Pattern(regexp = "([\\w\\W]+)\\s(\\d+)", message = "Modelo de endereço inválido.")
    public String endereco;

    @Column
    @NotNull(message = "Campo não pode estar vazio")
    @Pattern(regexp = "^(?:(?:\\+|00)?(55)\\s?)?(?:(?:\\(?[1-9][0-9]\\)?)?\\s?)?(?:((?:9\\d|[2-9])\\d{3})-?(\\d{4}))$", message = "Telefone inválido.")
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
            this.cpf = cpf;
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

    public ClienteModel(String nome, String cpf, String endereco, String telefone) {
        this.nome = nome;
        this.setCpf(cpf);
        this.endereco = endereco;
        this.telefone = telefone;
    }

    public ClienteModel() {
    }

}

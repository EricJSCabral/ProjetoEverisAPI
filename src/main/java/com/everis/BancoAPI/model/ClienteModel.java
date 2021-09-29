package com.everis.BancoAPI.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
//import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "clientes")
public class ClienteModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer codigo;

    public String nome;

    public String cpf;

    public String endereco;

    public String telefone;

    public ClienteModel(String nome, String cpf, String endereco, String telefone) {
        this.nome = nome;
        this.setCpf(cpf);
        this.endereco = endereco;
        this.telefone = telefone;
    }
}

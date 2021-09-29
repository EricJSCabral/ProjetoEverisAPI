package com.everis.BancoAPI.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteResponse {

    private Integer codigo;
    private String nome;
    private String cpf;
    private String endereco;
    private String telefone;
}

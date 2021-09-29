package com.everis.BancoAPI.dto.response;

import com.everis.BancoAPI.model.ClienteModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClienteDeleteResponse {

    private final String mensagem = "Cliente deletado com sucesso.";
    private ClienteModel cliente;
}

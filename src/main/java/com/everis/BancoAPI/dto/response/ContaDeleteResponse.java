package com.everis.BancoAPI.dto.response;

import com.everis.BancoAPI.model.ClienteModel;
import com.everis.BancoAPI.model.ContaModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContaDeleteResponse {

    private final String mensagem = "Conta deletada com sucesso.";
    private ContaModel conta;
}

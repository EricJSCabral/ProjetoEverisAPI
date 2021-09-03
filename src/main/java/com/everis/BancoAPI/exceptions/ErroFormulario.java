package com.everis.BancoAPI.exceptions;

public class ErroFormulario {

    private String campo;
    private String erro;

    public ErroFormulario(String campo, String erro) {
        this.campo = campo;
        this.erro = erro;
    }

    public String getCampo() {
        return campo;
    }

    public String getErro() {
        return erro;
    }

}

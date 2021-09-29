package com.everis.BancoAPI.exceptions;

public class ClienteExistente extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ClienteExistente(String mensagem){
        super(mensagem);
    }
}

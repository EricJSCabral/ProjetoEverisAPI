package com.everis.BancoAPI.exceptions;

public class ClienteNaoEncontrado extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ClienteNaoEncontrado(String message){
        super(message);
    }
}

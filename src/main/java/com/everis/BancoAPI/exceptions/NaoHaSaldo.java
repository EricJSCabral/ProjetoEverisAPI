package com.everis.BancoAPI.exceptions;

public class NaoHaSaldo extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NaoHaSaldo(String mensagem){
        super(mensagem);
    }
}

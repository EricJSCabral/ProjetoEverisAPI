package com.everis.BancoAPI.exceptions;

public class ContaExistente extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ContaExistente(String mensagem){
        super(mensagem);
    }
}

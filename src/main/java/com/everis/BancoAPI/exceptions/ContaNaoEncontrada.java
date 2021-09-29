package com.everis.BancoAPI.exceptions;

public class ContaNaoEncontrada extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ContaNaoEncontrada(String mensagem){
        super(mensagem);
    }
}

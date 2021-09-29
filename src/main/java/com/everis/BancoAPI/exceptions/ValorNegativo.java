package com.everis.BancoAPI.exceptions;

public class ValorNegativo extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ValorNegativo(String mensagem){
            super(mensagem);
        }
}

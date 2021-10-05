package com.everis.BancoAPI.exceptions;

public class CodigoNaoEncontrado extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CodigoNaoEncontrado(String message){
        super(message);
    }
}

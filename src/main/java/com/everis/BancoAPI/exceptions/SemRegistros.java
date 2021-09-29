package com.everis.BancoAPI.exceptions;

public class SemRegistros extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public SemRegistros(String message){
        super(message);
    }
}

package com.everis.BancoAPI.exceptions;

import com.everis.BancoAPI.model.ContaModel;
import org.json.simple.JSONObject;

import java.util.List;

public class ContaAtrelada extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public ContaAtrelada(String message){
        super(message);
    }
}

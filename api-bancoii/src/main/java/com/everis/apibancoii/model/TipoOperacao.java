package com.everis.apibancoii.model;

public enum TipoOperacao {
    SAQUE("Saque"), DEPOSITO("Depósito"), TRANSFERENCIA("Transferência");

    private String desc;

    TipoOperacao(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}

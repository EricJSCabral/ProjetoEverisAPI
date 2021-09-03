package com.everis.BancoAPI.model;

public enum TipoOperacao {
    SAQUE("Saque"), DEPOSITO("Depósito"), TRANSFERENCIA("Transferência Entrada"), TRANSFERENCIA_SAIDA("Transferência Saída");

    private String desc;

    TipoOperacao(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}

package com.everis.apibancoii.model;

public enum TipoConta {
    FISICA(1, "Pessoa Física", 5), JURIDICA(2,"Pessoa Jurídica", 50),
    GOVERNAMENTAL(3, "Pessoa Governamental", 250);

    private int codigo;
    private String descricao;
    private int saques;

    TipoConta(int i, String s, int sa) {
        this.codigo = i;
        this.descricao = s;
        this.saques = sa;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getSaques() {
        return saques;
    }
}

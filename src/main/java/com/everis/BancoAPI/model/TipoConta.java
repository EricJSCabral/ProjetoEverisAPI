package com.everis.BancoAPI.model;

public enum TipoConta {
    FISICA(1, "Pessoa Física", 5, 10), JURIDICA(2,"Pessoa Jurídica", 50, 10),
    GOVERNAMENTAL(3, "Pessoa Governamental", 250, 20);

    private int codigo;
    private String descricao;
    private int saques;
    public int taxa;

    TipoConta(int i, String s, int sa, int ta) {
        this.codigo = i;
        this.descricao = s;
        this.saques = sa;
        this.taxa = ta;
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

    public int getTaxa() {
        return taxa;
    }
}

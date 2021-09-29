package com.everis.BancoAPI.dto.request;

import com.everis.BancoAPI.model.ClienteModel;
import com.everis.BancoAPI.model.TipoConta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContaRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer codigo;

    @Column
    @NotNull(message = "Campo não pode estar vazio")
    @Size(min=5, max=5, message = "Deve conter apenas 5 digitos")
    @Pattern(regexp = "[0-9]+", message = "Formato inválido. Apenas números são aceitos.")
    private String agencia;

    @Column
    @NotNull(message = "Campo não pode estar vazio")
    @Size(min=4, max=4, message = "Deve conter apenas 4 digitos")
    @Pattern(regexp = "[0-9]+", message = "Formato inválido. Apenas números são aceitos.")
    private String numero;

    @Column
    @NotNull(message = "Campo não pode estar vazio")
    @Size(min=1, max=1, message = "Deve conter apenas 1 digitos")
    @Pattern(regexp = "[0-9]+", message = "Formato inválido. Apenas números são aceitos.")
    private String digitoVerificador;

    @Column
    @NotNull(message = "O campo não pode estar vazio")
    @Enumerated(EnumType.ORDINAL)
    private TipoConta tipo;

    @ManyToOne
    @JoinColumn(name = "clientes_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ClienteModel cliente;

    @Column
    @NotNull
    private double saldo;

    @Column
    @NotNull
    private int saques;

}

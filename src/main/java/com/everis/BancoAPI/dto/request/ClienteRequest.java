package com.everis.BancoAPI.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer codigo;

    @Column
    @NotNull(message = "Campo não pode estar vazio")
    @Size(min=5, max=50, message = "O tamanho é inválido")
    public String nome;

    @Column
    @CPF(message = "CPF inválido")
    public String cpf;

    @Column
    @NotNull(message = "Campo não pode estar vazio")
    @Size(min=10, max=50, message = "O tamanho é inválido")
    @Pattern(regexp = "([\\w\\W]+)\\s(\\d+)", message = "Modelo de endereço inválido.")
    public String endereco;

    @Column
    @NotNull(message = "Campo não pode estar vazio")
    @Pattern(regexp = "^(?:(?:\\+|00)?(55)\\s?)?(?:(?:\\(?[1-9][0-9]\\)?)?\\s?)?(?:((?:9\\d|[2-9])\\d{3})-?(\\d{4}))$", message = "Telefone inválido.")
    public String telefone;
}

package com.everis.BancoAPI.repository;

import com.everis.BancoAPI.model.ClienteModel;
import com.everis.BancoAPI.model.ContaModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContaRepository extends JpaRepository<ContaModel, Integer> {

    Optional<ContaModel> findByNumero(String numero);
    List<ContaModel> findAllByCliente(ClienteModel cliente);

//    Optional<ContaModel> findAllByCliente(int cliente);
}

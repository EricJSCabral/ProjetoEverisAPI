package com.everis.BancoAPI.repository;

import com.everis.BancoAPI.model.ClienteModel;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface ClienteRepository extends JpaRepository<ClienteModel, Integer> {
       Optional<ClienteModel> findByCpf(String cpf);
}

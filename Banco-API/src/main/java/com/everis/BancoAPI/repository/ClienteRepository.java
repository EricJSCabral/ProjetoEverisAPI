package com.everis.BancoAPI.repository;

import com.everis.BancoAPI.model.ClienteModel;
import org.springframework.data.repository.CrudRepository;

public interface ClienteRepository extends CrudRepository<ClienteModel, Integer> {

}

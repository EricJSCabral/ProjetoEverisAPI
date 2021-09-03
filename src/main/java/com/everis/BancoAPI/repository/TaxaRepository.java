package com.everis.BancoAPI.repository;

import com.everis.BancoAPI.model.ContaModel;
import org.springframework.data.repository.CrudRepository;

public interface TaxaRepository extends CrudRepository<ContaModel, Integer> {
}

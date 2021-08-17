package com.everis.BancoAPI.repository;

import com.everis.BancoAPI.model.OperacoesModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OperacoesRepository extends CrudRepository<OperacoesModel, Integer> {

}

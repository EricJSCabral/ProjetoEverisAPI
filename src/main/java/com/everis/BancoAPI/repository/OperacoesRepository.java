package com.everis.BancoAPI.repository;

import com.everis.BancoAPI.model.OperacoesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OperacoesRepository extends JpaRepository<OperacoesModel, Integer> {

    List<OperacoesModel> findAllByNumeroConta(String numeroConta);
}

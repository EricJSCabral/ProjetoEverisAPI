package com.everis.BancoAPI.service;

import com.everis.BancoAPI.model.TaxaModel;
import com.everis.BancoAPI.repository.ClienteRepository;
import com.everis.BancoAPI.repository.TaxaRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@WebAppConfiguration
public class TaxaServiceTest {

//    @Autowired
//    private TaxaModel taxaModel;
    @MockBean
    private TaxaRepository taxaRepository;

    @Test
    public void aplicarTaxa(){

    }

}
package com.everis.BancoAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

import static com.everis.BancoAPI.kafka.KafkaInit.iniciaKafka;
//import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
public class BancoApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BancoApiApplication.class, args);
		iniciaKafka();
	}

}

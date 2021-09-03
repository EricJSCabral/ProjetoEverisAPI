package com.everis.BancoAPI.kafka;

import java.io.IOException;

public class KafkaInit {

    public static void iniciaKafka(){
        ProcessBuilder zookeeper = new ProcessBuilder("C:\\Kafka\\batch\\Start_Zookeeper.bat");
		ProcessBuilder server1 = new ProcessBuilder("C:\\Kafka\\batch\\Start_Kafka_Server1.bat");
		ProcessBuilder server2 = new ProcessBuilder("C:\\Kafka\\batch\\Start_Kafka_Server2.bat");
		try {
			zookeeper.start();
			server1.start();
			server2.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}

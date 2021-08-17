package com.everis.BancoAPI.kafka;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaProducerSaques {
    public void EnviarDadosClienteSaque(int id) throws ExecutionException, InterruptedException {
        var producer = new org.apache.kafka.clients.producer.KafkaProducer<Integer, Integer>(properties());
        var value = id;
        var record = new ProducerRecord<>("CONTROLE_SAQUES", value, value);
        producer.send(record, (data, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                return;
            }
            //observer
            System.out.println("deu certo");
        }).get();
    }
    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        return properties;
    }
}
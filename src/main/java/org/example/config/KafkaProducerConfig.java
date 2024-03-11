package org.example.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Конфигурационнй класс Spring для настройки и создания Kafka Producer'а.
 */
@Configuration
public class KafkaProducerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Метод создает и настраивает конфигурацию для Kafka Producer'а, устанавливает адрес Bootstrap серверов Kafka, а также указывает классы сериализации для ключей и значений сообщений
     * @return конфигурацию для настройки Kafka Producer'а, представленую в виде пар ключ-значение
     */
    public Map<String, Object> producerConfig() {
        HashMap<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    /**
     * Метод создает и возвращает фабрику ProducerFactory Kafka Producer'а, используя конфигурацию, созданную методом producerConfig()
     * @return интерфейс из библиотеки Spring Kafka, предназначенный для создания и настройки объектов-производителей Producers Kafka
     */
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    /**
     * Метод создает KafkaTemplate, используя producerFactory
     * @param producerFactory фабрика Kafka Producer'а для создания Kafka Template
     * @return KafkaTemplate, который предоставляет удобный способ отправки сообщений в Kafka
     */
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}

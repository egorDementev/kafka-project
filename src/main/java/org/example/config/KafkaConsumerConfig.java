package org.example.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.logic.Orders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * Конфигурационный класс Spring для настройки и создания Kafka Consumer'а
 */
@Configuration
public class KafkaConsumerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Метод создает и настраивает конфигурацию для Kafka Consumer'а, устанавливает адрес Bootstrap серверов Kafka, а также указывает классы десериализации для ключей и значений сообщений
     * @return объект, который содержит конфигурацию для настройки Kafka Consumer'а, конфигурация представлена в виде пар ключ-значение
     */
    public Map<String, Object> consumerConfig() {
        HashMap<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    /**
     * Метод создает и возвращает фабрику ConsumerFactory Kafka Consumer'а, используя конфигурацию, созданную методом consumerConfig()
     * @return интерфейс из библиотеки Spring Kafka, который предназначен для создания и настройки объектов-потребителей Consumers Kafka
     */
    @Bean
    public ConsumerFactory<String, Orders> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }

    /**
     * Метод создает и возвращает фабрику слушателей Kafka
     * @param consumerFactory фабрика Kafka Consumer'а consumerFactory
     * @return ConcurrentKafkaListenerContainerFactory для обеспечения параллельной обработки сообщений
     */
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Orders>> factory (
            ConsumerFactory<String, Orders> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, Orders> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}

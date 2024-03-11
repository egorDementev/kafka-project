package org.example.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Конфигурационный класс Spring для определения темы (topic) в Apache Kafka
 */
@Configuration
public class KafkaTopicConfig {

    /**
     * Метод создает и настраивает новую тему Kafka с именем "sushi-bar" и 3 разделами (partitions)
     * @return новую тему
     */
    @Bean
    public NewTopic myTopic() {
        return TopicBuilder.name("sushi-bar").partitions(3).build();
    }
}

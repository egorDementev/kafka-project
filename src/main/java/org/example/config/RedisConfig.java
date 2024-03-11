package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Конфигурационный класс Spring для настройки подключения к базе данных Redis и определения RedisTemplate, который предоставляет доступ к операциям Redis
 */
@Configuration
@EnableRedisRepositories
public class RedisConfig {

    /**
     * Метод создает и настраивает подключение к базе данных Redis с помощью класса JedisConnectionFactory
     * @return фабрика соединений с базой данных Redis
     */
    @Bean
    public JedisConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName("localhost");
        configuration.setPort(6379);
        return new JedisConnectionFactory(configuration);
    }

    /**
     * Метод создает RedisTemplate, который используется для выполнения операций с данными в Redis, определяет сериализаторы ключей и значений для Redis:
     * StringRedisSerializer() используется для сериализации строковых ключей и значений;
     * JdkSerializationRedisSerializer() используется для сериализации объектов;
     * setEnableTransactionSupport(true) включает поддержку транзакций в RedisTemplate;
     * @return RedisTemplate, который можно использовать для выполнения операций в базе данных Redis
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new JdkSerializationRedisSerializer());
        template.setValueSerializer(new JdkSerializationRedisSerializer());
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        return template;
    }
}

package com.sparklecow.dark_engine_protocol.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    /**
     * Configures and creates the RedisTemplate bean, which is the main
     * interface for interacting with the Redis server.
     * @param connectionFactory Automatically provided by Spring, contains connection details (host, port).
     * @return The configured RedisTemplate.
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {

        // Instantiate the Template: This is the core Spring Data Redis class.
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        // Connect the template to the actual Redis server instance.
        template.setConnectionFactory(connectionFactory);

        // Configure String Serializer for Keys
        // Keys in Redis are always Strings (e.g., "game:positions", "player-123").
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();

        // Applies the String serializer to the main key (KEY) and nested keys (HashKeys).
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        // Configure JSON Serializer for Values
        // RedisSerializer.json() is the modern, non-deprecated way to use Jackson
        // for serialization. It automatically adds type information to the JSON
        // string, allowing Spring to reconstruct the correct Java object (e.g., PlayerPosition)
        // when reading the data back from Redis.
        RedisSerializer<Object> jsonSerializer = RedisSerializer.json();

        // Applies the JSON serializer to the main value (VALUE) and nested values (HashValues).
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);

        // Ensure all properties are set before the template is used.
        template.afterPropertiesSet();
        return template;
    }
}
package com.sparklecow.dark_engine_protocol.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    /*This method creates a new topic into kafka.*/
    @Bean
    public NewTopic statsTopic() {

        return TopicBuilder
                // Name of the Kafka topic to be created
                .name("stats-updated")
                // Number of partitions for the topic.
                // More partitions = more parallelism for consumers
                .partitions(3)
                // Number of replicas for each partition.
                .replicas(1)
                // Builds the NewTopic instance
                .build();
    }
}

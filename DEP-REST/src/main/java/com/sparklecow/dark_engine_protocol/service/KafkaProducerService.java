package com.sparklecow.dark_engine_protocol.service;

import com.sparklecow.dark_engine_protocol.entities.dtos.StatsEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TOPIC = "stats-updated";

    public void sendStats(StatsEvent statsEvent) {

        String message = objectMapper.writeValueAsString(statsEvent);

        kafkaTemplate.send(
                TOPIC,
                message
        );
    }
}
package com.sparklecow.dark_engine_protocol.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparklecow.dark_engine_protocol.models.MonsterDeathEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TOPIC = "stats";

    public void sendStats(MonsterDeathEvent monsterDeathEvent) {

        try {
            String message = objectMapper.writeValueAsString(monsterDeathEvent);

            kafkaTemplate.send(
                    TOPIC,
                    monsterDeathEvent.monsterId(),
                    message
            );

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing MonsterDeathEvent", e);
        }
    }
}

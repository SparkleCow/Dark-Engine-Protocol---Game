package com.sparklecow.dark_engine_protocol.listeners;

import com.sparklecow.dark_engine_protocol.entities.dtos.MonsterDeathEvent;
import com.sparklecow.dark_engine_protocol.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final StatsService statsService;

    @KafkaListener(
            topics = "stats",
            groupId = "stats"
    )
    public void listen(String message) {
        ObjectMapper mapper = new ObjectMapper();
        MonsterDeathEvent event = mapper.readValue(message, MonsterDeathEvent.class);
        statsService.applyMonsterDeath(event);
        System.out.println("Message received: " + message);
    }
}

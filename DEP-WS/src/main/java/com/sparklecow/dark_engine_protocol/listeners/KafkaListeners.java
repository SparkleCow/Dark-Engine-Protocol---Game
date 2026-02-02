package com.sparklecow.dark_engine_protocol.listeners;

import com.sparklecow.dark_engine_protocol.models.StatsEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaListeners {

    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(
            topics = "stats-updated",
            groupId = "stats-updated"
    )
    public void listen(String message) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        log.info("RAW KAFKA MESSAGE: {}", message);

        StatsEvent statsEvent = mapper.readValue(message, StatsEvent.class);

        log.info("/queue/stats/{}", statsEvent.username());
        messagingTemplate.convertAndSend(
                "/queue/stats/" + statsEvent.username(),
                statsEvent
        );
    }
}

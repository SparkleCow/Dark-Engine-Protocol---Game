package com.sparklecow.dark_engine_protocol.services;

import com.sparklecow.dark_engine_protocol.models.MonsterDeathEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsService {

    private final KafkaProducerService kafkaProducerService;

    public void addExperience(MonsterDeathEvent event) {
        kafkaProducerService.sendStats(event);

        log.info(
                "STATS UPDATE | playerId={} | xp={} | honor={} | companyPoints={} | credits={} | astato={}",
                event.killerPlayerId(),
                event.experience(),
                event.honor(),
                event.companyPoints(),
                event.credits(),
                event.astato()
        );
    }
}

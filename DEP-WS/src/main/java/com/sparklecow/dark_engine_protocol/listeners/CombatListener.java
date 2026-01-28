package com.sparklecow.dark_engine_protocol.listeners;

import com.sparklecow.dark_engine_protocol.models.MonsterDeathEvent;
import com.sparklecow.dark_engine_protocol.services.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CombatListener {

    private final StatsService statsService;

    @EventListener
    public void onMonsterDeath(MonsterDeathEvent event) {

        statsService.addExperience(
                event.killerPlayerId(),
                event.experience(),
                event.honor(),
                event.companyPoints(),
                event.credits(),
                event.astato()
        );

//        missionService.notifyKill(
//                event.killerPlayerId(),
//                event.monsterId()
//        );
    }
}

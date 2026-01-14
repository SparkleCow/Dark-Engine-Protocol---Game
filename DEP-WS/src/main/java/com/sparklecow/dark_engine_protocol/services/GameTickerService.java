package com.sparklecow.dark_engine_protocol.services;

import com.sparklecow.dark_engine_protocol.entities.Position;
import com.sparklecow.dark_engine_protocol.models.MonsterSnapshot;
import com.sparklecow.dark_engine_protocol.models.WorldSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameTickerService {

    private final PositionService positionService;
    private final SimpMessagingTemplate messagingTemplate;
    private final MonsterService monsterService;

    @Scheduled(fixedRate = 50)
    public void gameTick() {

        long now = System.currentTimeMillis();
        monsterService.updateAll(now);

        List<Position> players = positionService.getAllPositions();
        List<MonsterSnapshot> monsters = monsterService.getMonsterSnapshots();

        WorldSnapshot snapshot = new WorldSnapshot(players, monsters);

        messagingTemplate.convertAndSend("/topic/sync/world", snapshot);
    }
}
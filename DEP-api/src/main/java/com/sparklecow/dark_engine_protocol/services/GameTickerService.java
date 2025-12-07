package com.sparklecow.dark_engine_protocol.services;

import com.sparklecow.dark_engine_protocol.entities.Position;
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

    @Scheduled(fixedRate = 50) // 50 ms
    public void gameTick() {

        List<Position> allPositions = positionService.getAllPositions();

        if (!allPositions.isEmpty()) {
            messagingTemplate.convertAndSend("/topic/sync/all-positions", allPositions);
        }
    }
}
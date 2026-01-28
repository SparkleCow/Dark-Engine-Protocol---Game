package com.sparklecow.dark_engine_protocol.controllers;

import com.sparklecow.dark_engine_protocol.models.AttackMessage;
import com.sparklecow.dark_engine_protocol.models.Position;
import com.sparklecow.dark_engine_protocol.services.CombatService;
import com.sparklecow.dark_engine_protocol.services.PositionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

    private final PositionService positionService;
    private final SimpMessagingTemplate messagingTemplate;
    private final CombatService combatService;

    /**
     * Handles movement messages sent by clients.
     * Clients send messages to: /app/move
     * The authenticated username is retrieved from the STOMP session attributes,
     * previously set by the WebSocketAuthChannelInterceptor during CONNECT.
     * No database access or authentication logic is performed here.
     */
    @MessageMapping("/move")
    public void handleMovement(Position position, SimpMessageHeaderAccessor headerAccessor) {
        Object usernameAttr = headerAccessor.getSessionAttributes().get("username");

        if (usernameAttr == null) {
            log.warn("Movement received without authenticated username");
            return;
        }

        String username = usernameAttr.toString();

        log.info(
                "User {} moved to X={} Y={}",
                username,
                position.getX(),
                position.getY()
        );

        // Associate the position update with the username
        positionService.updatePosition(username, position);
    }

    @MessageMapping("/attack")
    public void handleCombat(AttackMessage msg, SimpMessageHeaderAccessor headerAccessor){
        Object usernameAttr = headerAccessor.getSessionAttributes().get("username");

        if (usernameAttr == null) {
            log.warn("Attack order received without authenticated username");
            return;
        }

        combatService.attackMonster(usernameAttr.toString(), msg.getMonsterId());


    }

    // TODO: Add @MessageMapping handlers for shooting, pickups, combat, etc.
}

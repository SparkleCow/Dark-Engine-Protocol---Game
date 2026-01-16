package com.sparklecow.dark_engine_protocol.listeners;

import com.sparklecow.dark_engine_protocol.models.Position;
import com.sparklecow.dark_engine_protocol.services.PositionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final PositionService positionService;

    /**
     * Listens for WebSocket/STOMP session disconnect events.
     *
     * When a client disconnects:
     * - The authenticated username is retrieved from the STOMP session attributes.
     * - The player's last known position is fetched from Redis.
     * - The position is removed from Redis to avoid ghost players.
     *
     * Persistence to the database is intentionally deferred and will be handled later.
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {

        StompHeaderAccessor headerAccessor =
                MessageHeaderAccessor.getAccessor(event.getMessage(), StompHeaderAccessor.class);

        if (headerAccessor == null || headerAccessor.getSessionAttributes() == null) {
            log.warn("Session disconnected with no STOMP headers available. SessionId={}", event.getSessionId());
            return;
        }

        // Username was stored by WebSocketAuthChannelInterceptor on CONNECT
        String username =
                (String) headerAccessor.getSessionAttributes().get("username");

        if (username == null) {
            log.warn(
                    "WebSocket session {} disconnected but no username found in session attributes.",
                    event.getSessionId()
            );
            return;
        }

        log.info("Player '{}' disconnected. Cleaning up runtime state.", username);

        // Fetch last known position from Redis (runtime state)
        Position finalPosition = positionService.getPosition(username);

        if (finalPosition != null) {
            // For now: only clean up Redis
            positionService.deletePosition(username);

            log.info(
                    "Runtime position for player '{}' removed from Redis.",
                    username
            );
        } else {
            log.warn(
                    "Player '{}' disconnected but no position was found in Redis.",
                    username
            );
        }
    }
}

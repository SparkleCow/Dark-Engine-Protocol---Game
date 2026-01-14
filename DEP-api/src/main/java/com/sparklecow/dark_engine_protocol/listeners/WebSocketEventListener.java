package com.sparklecow.dark_engine_protocol.listeners;

import com.sparklecow.dark_engine_protocol.entities.Position;
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
     * Captura el evento de desconexión de cualquier sesión WebSocket/STOMP.
     * @param event El evento de desconexión de la sesión.
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {

        // 1. Obtener acceso a los headers y atributos de la sesión STOMP
        StompHeaderAccessor headerAccessor =
                MessageHeaderAccessor.getAccessor(event.getMessage(), StompHeaderAccessor.class);

        // 2. RECUPERAR EL PLAYER ID
        // La clave "PlayerId" fue almacenada por el WebSocketAuthChannelInterceptor.
        String playerIdString = (String) headerAccessor.getSessionAttributes().get("PlayerId");

        if (playerIdString != null) {
            log.info("Player {} disconnected. Initiating position save sequence.", playerIdString);

            // 3. Leer la última posición en tiempo real de Redis
            // El PositionService usará playerIdString para buscar la clave POS:1
            Position finalPosition = positionService.getPosition(playerIdString);

            if (finalPosition != null) {
                positionService.saveLastPosition(Long.parseLong(playerIdString), finalPosition);
                positionService.deletePositionFromRedis(playerIdString);
            } else {
                log.warn("Player {} disconnected, but no position found in Redis. Skip persistence.", playerIdString);
            }
        } else {
            log.warn("Unknown session disconnected (ID: {}) - PlayerId not found in attributes. Skipping save.", event.getSessionId());
        }
    }
}

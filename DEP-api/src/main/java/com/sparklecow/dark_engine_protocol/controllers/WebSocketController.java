package com.sparklecow.dark_engine_protocol.controllers;

import com.sparklecow.dark_engine_protocol.entities.LastPosition;
import com.sparklecow.dark_engine_protocol.entities.Player;
import com.sparklecow.dark_engine_protocol.entities.Position;
import com.sparklecow.dark_engine_protocol.services.PositionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

    private final PositionService positionService;
    private final SimpMessagingTemplate messagingTemplate; // Para el Broadcast

    /**
     * Maneja los mensajes de movimiento de los clientes.
     * El cliente envía a: /app/move (ver WebSocketConfig)
     * @param position El objeto Position enviado por Godot (serializado desde JSON).
     * @param headerAccessor Contiene información de la sesión (para autenticación).
     */
    @MessageMapping("/move")
    public void handleMovement(Position position, SimpMessageHeaderAccessor headerAccessor) {


        // 1. Obtener el Player ID Autenticado y Seguro (Reemplazar con tu lógica de autenticación)
        // **IMPORTANTE**: No confíes en el ID enviado por el cliente. Debe venir de la sesión.
        // Por ahora, usaremos el que viene en el objeto Position para pruebas:
        log.info("Jugador {} se movió a X={} Y={}", position.getPlayerId(), position.getX(), position.getY());

        Long playerId = position.getPlayerId();

        if (playerId != null) {

            // 2. Actualizar la posición en Redis
            positionService.updatePosition(position);

            // 3. Notificación Inmediata (Broadcast)
            // Envía la posición actualizada a todos los clientes suscritos al topic.
            // Los clientes se suscriben a: /topic/updates/movement
            messagingTemplate.convertAndSend("/topic/updates/movement", position);
        }
    }

    @MessageMapping("/status/join")
    public void handlePlayerJoin(@AuthenticationPrincipal Player player) {

        Long playerId = player.getId();

        // 1. Buscar la última posición guardada en PostgreSQL
        LastPosition lastPosition = positionService.findLastPosition(playerId);

        // 2. Crear/Cargar el objeto Position inicial
        Position initialPosition;

        if (lastPosition != null) {
            // Cargar posición guardada
            initialPosition = new Position(
                    playerId,
                    lastPosition.getX(),
                    lastPosition.getY(),
                    lastPosition.getMapId(),
                    lastPosition.getAngle()
            );
        } else {
            // Usar posición por defecto (primer inicio de sesión)
            initialPosition = new Position(playerId, 0.0, 0.0, 1, 0.0);

            // Opcional: Guardar esta posición inicial por defecto en PostgreSQL/Redis
        }

        // 3. Guardar en Redis para el estado en tiempo real
        positionService.updatePosition(initialPosition);

        // 4. Enviar la posición inicial de vuelta al cliente (topic privado)
        // Ejemplo de topic privado: /queue/player/status
        messagingTemplate.convertAndSendToUser(
                player.getUsername(), // Asegúrate de que el username es el identificador en el front
                "/queue/status",
                initialPosition
        );
    }

    // TODO: Crear métodos @MessageMapping para Disparos, Recolección, etc.
}

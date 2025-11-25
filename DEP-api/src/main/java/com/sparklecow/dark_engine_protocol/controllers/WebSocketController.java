package com.sparklecow.dark_engine_protocol.controllers;

import com.sparklecow.dark_engine_protocol.entities.Position;
import com.sparklecow.dark_engine_protocol.services.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
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

    // TODO: Crear métodos @MessageMapping para Disparos, Recolección, etc.
}

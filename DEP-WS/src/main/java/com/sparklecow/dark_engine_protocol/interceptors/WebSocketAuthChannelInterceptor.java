package com.sparklecow.dark_engine_protocol.interceptors;

import com.sparklecow.dark_engine_protocol.config.jwt.JwtUtils;
import com.sparklecow.dark_engine_protocol.entities.Player;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


/*Esto se hizo para interceptar el STOMP y ponerle el ID del usuario logueado a traves del jwt
  con el fin de poder usar ese ID para crear un position nuevo y guardarlo en la db.
  TODO Comentar esto bien :D
* */
@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketAuthChannelInterceptor implements ChannelInterceptor {

    private final JwtUtils jwtUtils;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authorizationHeader = accessor.getFirstNativeHeader("Authorization");

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String jwt = authorizationHeader.substring(7);

                try {
                    // 3. Obtener la autenticación
                    Authentication authentication = jwtUtils.getAuthentication(jwt);

                    // 4. Guardar la autenticación en la sesión
                    accessor.setUser(authentication);

                    // 5. OBTENER Y GUARDAR EL PLAYER ID:
                    // Ya que tu UserDetailsService devuelve la entidad Player
                    // que implementa UserDetails, casteamos a Player.

                    Object principal = authentication.getPrincipal();
                    if (principal instanceof Player) {
                        Player player = (Player) principal;

                        // Usamos el getter getId() de la entidad Player
                        Long playerId = player.getId();

                        // Guardar en los atributos de la sesión de WebSocket
                        accessor.getSessionAttributes().put("PlayerId", playerId.toString());

                        log.info("STOMP Session {} successfully authenticated for Player ID: {}",
                                accessor.getSessionId(), playerId);
                    } else {
                        throw new IllegalStateException("Principal is not a Player entity.");
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Invalid JWT token or authentication failure.", e);
                }
            }
        }
        return message;
    }
}
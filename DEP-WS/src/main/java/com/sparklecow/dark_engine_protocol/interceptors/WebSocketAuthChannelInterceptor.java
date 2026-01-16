package com.sparklecow.dark_engine_protocol.interceptors;

import com.sparklecow.dark_engine_protocol.config.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

/*
 * This interceptor intercepts incoming STOMP messages in order to authenticate
 * WebSocket connections using a JWT token.
 *
 * On STOMP CONNECT:
 * - The JWT is extracted from the "Authorization" header.
 * - The token is validated.
 * - The username (JWT subject) is extracted.
 * - The username is attached to the WebSocket session attributes.
 *
 * The username acts as the canonical identity at the WebSocket level.
 * Database access and Spring Security are intentionally avoided here.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketAuthChannelInterceptor implements ChannelInterceptor {

    private final JwtUtils jwtUtils;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) {
            return message;
        }

        // Only authenticate on initial CONNECT frame
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String authorizationHeader =
                    accessor.getFirstNativeHeader("Authorization");

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                log.warn("Missing or invalid Authorization header on STOMP CONNECT");
                throw new RuntimeException("Missing Authorization header");
            }

            String jwt = authorizationHeader.substring(7);

            try {
                // Validate token integrity and expiration
                if (!jwtUtils.validateToken(jwt)) {
                    throw new RuntimeException("JWT token is invalid or expired");
                }

                // Extract username (subject)
                String username = jwtUtils.extractUsername(jwt);

                if (username == null || username.isBlank()) {
                    throw new RuntimeException("JWT does not contain a valid username");
                }

                // Store username in WebSocket session
                accessor.getSessionAttributes().put("username", username);

                log.info(
                        "STOMP session {} authenticated for username: {}",
                        accessor.getSessionId(),
                        username
                );

            } catch (Exception e) {
                log.error("WebSocket JWT authentication failed", e);
                throw new RuntimeException(
                        "Invalid JWT token or authentication failure",
                        e
                );
            }
        }
        return message;
    }
}
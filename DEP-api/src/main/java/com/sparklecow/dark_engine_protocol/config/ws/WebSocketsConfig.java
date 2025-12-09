package com.sparklecow.dark_engine_protocol.config.ws;

import com.sparklecow.dark_engine_protocol.interceptors.WebSocketAuthChannelInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketsConfig implements WebSocketMessageBrokerConfigurer {

    private final WebSocketAuthChannelInterceptor authInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // This enables a simple in-memory message broker
        // Clients can subscribe to /topic/... or /queue/...
        config.enableSimpleBroker("/topic", "/queue");

        // Application destination prefix
        // Messages sent by clients to /app/... go to @MessageMapping methods
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Client connects here: http://localhost:8080/ws
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();           // fallback if WS not available
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // Register interceptor before connect message
        registration.interceptors(authInterceptor);
    }
}
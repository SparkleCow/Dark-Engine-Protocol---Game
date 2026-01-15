package com.sparklecow.dark_engine_protocol_auth.entites;

public record PlayerCreationEventDto(
        String username,
        String email
) {}
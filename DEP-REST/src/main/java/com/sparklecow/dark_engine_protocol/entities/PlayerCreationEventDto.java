package com.sparklecow.dark_engine_protocol.entities;

public record PlayerCreationEventDto(
        String username,
        String email
) {}
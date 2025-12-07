package com.sparklecow.dark_engine_protocol.models;

public record AuthRequestDto(
        String username,
        String password
) {
}

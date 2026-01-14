package com.sparklecow.dark_engine_protocol.models.dtos;

public record AuthRequestDto(
        String username,
        String password
) {
}

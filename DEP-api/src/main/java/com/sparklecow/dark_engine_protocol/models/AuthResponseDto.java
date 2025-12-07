package com.sparklecow.dark_engine_protocol.models;

public record AuthResponseDto(
        String jwt,
        Long userId
) {
}

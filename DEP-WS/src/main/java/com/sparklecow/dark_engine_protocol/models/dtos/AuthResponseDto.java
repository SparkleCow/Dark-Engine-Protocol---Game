package com.sparklecow.dark_engine_protocol.models.dtos;

public record AuthResponseDto(
        String jwt,
        Long playerId
) {
}

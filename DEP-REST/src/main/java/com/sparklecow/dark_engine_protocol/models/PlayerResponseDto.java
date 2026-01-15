package com.sparklecow.dark_engine_protocol.models;

import com.sparklecow.dark_engine_protocol.entities.Inventory;
import com.sparklecow.dark_engine_protocol.entities.LastPosition;
import com.sparklecow.dark_engine_protocol.entities.Stats;

public record PlayerResponseDto(
        Long id,
        String username,
        String email,
        Stats stats,
        Inventory inventory,
        LastPosition lastPosition
) {
}
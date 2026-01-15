package com.sparklecow.dark_engine_protocol.service;

import com.sparklecow.dark_engine_protocol.entities.PlayerCreationEventDto;
import com.sparklecow.dark_engine_protocol.models.PlayerResponseDto;

public interface PlayerService {

    PlayerResponseDto findByUsername(String username);

    void createInitialPlayer(PlayerCreationEventDto playerCreationEventDto);
}

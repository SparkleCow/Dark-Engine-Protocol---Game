package com.sparklecow.dark_engine_protocol.service;

import com.sparklecow.dark_engine_protocol.entities.Player;
import com.sparklecow.dark_engine_protocol.entities.PlayerCreationEventDto;
import com.sparklecow.dark_engine_protocol.mappers.PlayerMapper;
import com.sparklecow.dark_engine_protocol.models.PlayerResponseDto;
import com.sparklecow.dark_engine_protocol.repositories.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerServiceImp implements PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;

    @Override
    public PlayerResponseDto findByUsername(String username) {
        Player player = playerRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Player not found"));
        return playerMapper.playerToDto(player);
    }

    @Override
    public void createInitialPlayer(PlayerCreationEventDto playerCreationEventDto) {

    }
}

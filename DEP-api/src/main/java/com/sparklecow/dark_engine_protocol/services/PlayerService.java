package com.sparklecow.dark_engine_protocol.services;

import com.sparklecow.dark_engine_protocol.entities.Player;
import com.sparklecow.dark_engine_protocol.mappers.PlayerMapper;
import com.sparklecow.dark_engine_protocol.models.PlayerResponseDto;
import com.sparklecow.dark_engine_protocol.repositories.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;

    public PlayerResponseDto findPlayer(Authentication authentication){
        Player player = (Player) authentication.getPrincipal();
        return playerMapper.playerToDto(player);
    }
}

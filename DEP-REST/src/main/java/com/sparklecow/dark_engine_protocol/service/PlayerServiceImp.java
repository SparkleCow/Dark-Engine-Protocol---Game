package com.sparklecow.dark_engine_protocol.service;

import com.sparklecow.dark_engine_protocol.entities.*;
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
        Stats stats = Stats.builder()
                .level(1)
                .experience(0L)
                .honor(0L)
                .companyPoints(0L)
                .credits(0L)
                .astato(0L)
                .build();

        Inventory inventory = Inventory.builder().build();

        LastPosition position = LastPosition.builder()
                .x(20)
                .y(20)
                .angle(0)
                .mapId(1)
                .build();

        playerRepository.save(Player.builder()
                .username(playerCreationEventDto.username())
                .email(playerCreationEventDto.email())
                .stats(stats)
                .inventory(inventory)
                .lastPosition(position)
                .build());
    }
}

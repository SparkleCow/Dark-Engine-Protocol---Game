package com.sparklecow.dark_engine_protocol.mappers;

import com.sparklecow.dark_engine_protocol.entities.Player;
import com.sparklecow.dark_engine_protocol.models.dtos.PlayerRequestDto;
import com.sparklecow.dark_engine_protocol.models.dtos.PlayerResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayerMapper {

    private final PasswordEncoder passwordEncoder;

    public PlayerResponseDto playerToDto(Player player) {
        if (player == null) return null;

        return new PlayerResponseDto(
                player.getId(),
                player.getUsername(),
                player.getEmail(),
                player.getStats(),
                player.getInventory(),
                player.getLastPosition()
        );
    }

    public Player dtoToPlayer(PlayerRequestDto dto) {
        if (dto == null) return null;

        return Player.builder()
                .username(dto.username())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                // This fields will be created later in a service or in game
                .stats(null)
                .inventory(null)
                .lastPosition(null)
                .build();
    }
}

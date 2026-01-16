package com.sparklecow.dark_engine_protocol.mappers;

import com.sparklecow.dark_engine_protocol.entities.Player;
import com.sparklecow.dark_engine_protocol.models.PlayerResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayerMapper {

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
}

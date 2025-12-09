package com.sparklecow.dark_engine_protocol.services;

import com.sparklecow.dark_engine_protocol.config.jwt.JwtUtils;
import com.sparklecow.dark_engine_protocol.entities.LastPosition;
import com.sparklecow.dark_engine_protocol.entities.Player;
import com.sparklecow.dark_engine_protocol.mappers.PlayerMapper;
import com.sparklecow.dark_engine_protocol.models.AuthRequestDto;
import com.sparklecow.dark_engine_protocol.models.AuthResponseDto;
import com.sparklecow.dark_engine_protocol.models.PlayerRequestDto;
import com.sparklecow.dark_engine_protocol.models.PlayerResponseDto;
import com.sparklecow.dark_engine_protocol.repositories.PlayerRepository;
import com.sparklecow.dark_engine_protocol.repositories.PositionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PlayerRepository playerRepository;
    private final PositionRepository positionRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PlayerMapper playerMapper;

    @Transactional
    public PlayerResponseDto register(PlayerRequestDto playerRequestDto) {

        Player player = playerMapper.dtoToPlayer(playerRequestDto);

        Player savedPlayer = playerRepository.save(player);
        Long playerId = savedPlayer.getId();

        LastPosition initialPosition = LastPosition.builder()
                .playerId(playerId)
                .mapId(1)
                .angle(0)
                .y(20)
                .x(20)
                .build();

        LastPosition savedPosition = positionRepository.save(initialPosition);

        savedPlayer.setLastPosition(savedPosition);
        return playerMapper.playerToDto(savedPlayer);
    }

    public AuthResponseDto login(AuthRequestDto authRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequestDto.username(),
                        authRequestDto.password()
                )
        );

        Player player = (Player) authentication.getPrincipal();
        String token = jwtUtils.generateToken(player);
        assert player != null;
        return new AuthResponseDto(token, player.getId());
    }
}
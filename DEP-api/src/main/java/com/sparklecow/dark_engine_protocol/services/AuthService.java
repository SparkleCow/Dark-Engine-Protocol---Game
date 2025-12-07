package com.sparklecow.dark_engine_protocol.services;

import com.sparklecow.dark_engine_protocol.config.jwt.JwtUtils;
import com.sparklecow.dark_engine_protocol.entities.Player;
import com.sparklecow.dark_engine_protocol.mappers.PlayerMapper;
import com.sparklecow.dark_engine_protocol.models.AuthRequestDto;
import com.sparklecow.dark_engine_protocol.models.AuthResponseDto;
import com.sparklecow.dark_engine_protocol.models.PlayerRequestDto;
import com.sparklecow.dark_engine_protocol.models.PlayerResponseDto;
import com.sparklecow.dark_engine_protocol.repositories.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PlayerRepository playerRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PlayerMapper playerMapper;

    public PlayerResponseDto register(PlayerRequestDto playerRequestDto) {
        Player player = playerRepository.save(playerMapper.dtoToPlayer(playerRequestDto));
        return playerMapper.playerToDto(player);
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
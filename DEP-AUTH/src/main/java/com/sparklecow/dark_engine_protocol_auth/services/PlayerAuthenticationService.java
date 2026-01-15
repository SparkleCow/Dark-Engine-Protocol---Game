package com.sparklecow.dark_engine_protocol_auth.services;

import com.sparklecow.dark_engine_protocol_auth.config.jwt.JwtUtils;
import com.sparklecow.dark_engine_protocol_auth.entites.*;
import com.sparklecow.dark_engine_protocol_auth.repositories.PlayerAuthenticationRepository;
import com.sparklecow.dark_engine_protocol_auth.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleResult;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PlayerAuthenticationService {

    private final PlayerAuthenticationRepository playerAuthenticationRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RoleRepository roleRepository;

    public void register(PlayerRegisterDto playerRegisterDto){

        Role playerRole = roleRepository.findByName(String.valueOf(RoleEnum.ROLE_PLAYER))
                .orElseThrow(() -> new IllegalStateException("ROLE_PLAYER not found"));

        PlayerAuthentication playerAuthentication = PlayerAuthentication
                .builder()
                .username(playerRegisterDto.username())
                .email(playerRegisterDto.email())
                .password(passwordEncoder.encode(playerRegisterDto.password()))
                .roles(List.of(playerRole))
                .build();

        playerAuthenticationRepository.save(playerAuthentication);
        // TODO notificar a REST que se creo este usuario y que el debe crear el player como tal
    }

    public AuthResponseDto login(PlayerLoginDto playerLogindto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        playerLogindto.username(),
                        playerLogindto.password()
                )
        );

        PlayerAuthentication player =
                (PlayerAuthentication) authentication.getPrincipal();

        assert player != null;
        Map<String, Object> claims = Map.of(
                "roles", player.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );
        String token = jwtUtils.generateToken(player, claims);

        return new AuthResponseDto(token);
    }

}

package com.sparklecow.dark_engine_protocol.controllers;

import com.sparklecow.dark_engine_protocol.models.dtos.AuthRequestDto;
import com.sparklecow.dark_engine_protocol.models.dtos.AuthResponseDto;
import com.sparklecow.dark_engine_protocol.models.dtos.PlayerRequestDto;
import com.sparklecow.dark_engine_protocol.mappers.PlayerMapper;
import com.sparklecow.dark_engine_protocol.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final PlayerMapper playerMapper;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody PlayerRequestDto dto) {
        authService.register(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto authRequestDto) {
        return ResponseEntity.ok(authService.login(authRequestDto));
    }
}

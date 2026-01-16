package com.sparklecow.dark_engine_protocol_auth.controllers;

import com.sparklecow.dark_engine_protocol_auth.entites.AuthResponseDto;
import com.sparklecow.dark_engine_protocol_auth.entites.PlayerLoginDto;
import com.sparklecow.dark_engine_protocol_auth.entites.PlayerRegisterDto;
import com.sparklecow.dark_engine_protocol_auth.services.PlayerAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final PlayerAuthenticationService playerAuthenticationService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody PlayerRegisterDto playerRegisterDto){
        playerAuthenticationService.register(playerRegisterDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody PlayerLoginDto playerLoginDto){
        return ResponseEntity.ok(playerAuthenticationService.login(playerLoginDto));
    }
}

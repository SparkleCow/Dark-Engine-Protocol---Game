package com.sparklecow.dark_engine_protocol_auth.controllers;

import com.sparklecow.dark_engine_protocol_auth.entites.PlayerRegisterDto;
import com.sparklecow.dark_engine_protocol_auth.services.PlayerAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final PlayerAuthenticationService playerAuthenticationService;

    @PutMapping("/register")
    public ResponseEntity<Void> register(PlayerRegisterDto playerRegisterDto){

    }
}

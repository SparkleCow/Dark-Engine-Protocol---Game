package com.sparklecow.dark_engine_protocol.controllers;

import com.sparklecow.dark_engine_protocol.models.PlayerResponseDto;
import com.sparklecow.dark_engine_protocol.services.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/player")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping
    public ResponseEntity<PlayerResponseDto> getPlayerInformation(Authentication authentication){
        return ResponseEntity.ok(playerService.findPlayer(authentication));
    }
}

package com.sparklecow.dark_engine_protocol.controllers;

import com.sparklecow.dark_engine_protocol.entities.PlayerCreationEventDto;
import com.sparklecow.dark_engine_protocol.models.PlayerResponseDto;
import com.sparklecow.dark_engine_protocol.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/player")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping("/me")
    public ResponseEntity<PlayerResponseDto> me(Authentication authentication){
        return ResponseEntity.ok(playerService.findByUsername(authentication.getName()));
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createPlayer(@RequestBody PlayerCreationEventDto playerCreationEventDto) {
        playerService.createInitialPlayer(playerCreationEventDto);
        return ResponseEntity.ok().build();
    }
}

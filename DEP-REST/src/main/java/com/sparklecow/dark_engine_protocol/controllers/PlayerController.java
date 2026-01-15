package com.sparklecow.dark_engine_protocol.controllers;

import com.sparklecow.dark_engine_protocol.entities.Player;
import com.sparklecow.dark_engine_protocol.service.PlayerService;
import com.sparklecow.dark_engine_protocol.service.PlayerServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/player")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping("/me")
    public ResponseEntity<Player> me(Authentication authentication){

        String username = authentication.getName();

        Player player = playerService.findByUsername(username);

        return ResponseEntity.ok(player);
    }
}

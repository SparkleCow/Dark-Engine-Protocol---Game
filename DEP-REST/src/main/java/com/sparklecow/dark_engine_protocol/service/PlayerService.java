package com.sparklecow.dark_engine_protocol.service;

import com.sparklecow.dark_engine_protocol.entities.Player;

public interface PlayerService {

    Player findByUsername(String username);
}

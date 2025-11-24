package com.sparklecow.dark_engine_protocol.repositories;

import com.sparklecow.dark_engine_protocol.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long>{

}

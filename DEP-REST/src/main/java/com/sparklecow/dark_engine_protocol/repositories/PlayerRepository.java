package com.sparklecow.dark_engine_protocol.repositories;

import com.sparklecow.dark_engine_protocol.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long>{

    Optional<Player> findByUsername(String username);


}

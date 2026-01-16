package com.sparklecow.dark_engine_protocol.repositories;

import com.sparklecow.dark_engine_protocol.entities.Stats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatsRepository extends JpaRepository<Stats, Long> {
}

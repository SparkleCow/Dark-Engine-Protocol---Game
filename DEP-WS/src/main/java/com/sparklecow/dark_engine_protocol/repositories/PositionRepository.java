package com.sparklecow.dark_engine_protocol.repositories;

import com.sparklecow.dark_engine_protocol.entities.LastPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<LastPosition, Long> {

}

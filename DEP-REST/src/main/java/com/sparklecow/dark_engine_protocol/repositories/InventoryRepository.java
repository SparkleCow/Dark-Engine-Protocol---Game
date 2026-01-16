package com.sparklecow.dark_engine_protocol.repositories;

import com.sparklecow.dark_engine_protocol.entities.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}

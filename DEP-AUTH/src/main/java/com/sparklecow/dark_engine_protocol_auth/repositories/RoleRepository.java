package com.sparklecow.dark_engine_protocol_auth.repositories;

import com.sparklecow.dark_engine_protocol_auth.entites.Role;
import com.sparklecow.dark_engine_protocol_auth.entites.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleEnum name);
}

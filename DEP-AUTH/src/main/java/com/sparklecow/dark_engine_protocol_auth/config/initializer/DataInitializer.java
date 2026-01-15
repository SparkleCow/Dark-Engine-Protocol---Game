package com.sparklecow.dark_engine_protocol_auth.config.initializer;

import com.sparklecow.dark_engine_protocol_auth.entites.Role;
import com.sparklecow.dark_engine_protocol_auth.entites.RoleEnum;
import com.sparklecow.dark_engine_protocol_auth.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;

    @Bean
    CommandLineRunner initRoles() {
        return args -> createRoleIfNotExists(RoleEnum.ROLE_PLAYER);
    }

    private void createRoleIfNotExists(RoleEnum roleEnum) {
        roleRepository.findByName(roleEnum)
                .orElseGet(() -> roleRepository.save(
                        Role.builder()
                                .name(roleEnum)
                                .build()
                ));
    }
}

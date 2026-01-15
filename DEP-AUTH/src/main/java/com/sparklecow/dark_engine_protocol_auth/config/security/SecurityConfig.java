package com.sparklecow.dark_engine_protocol_auth.config.security;

import com.sparklecow.dark_engine_protocol_auth.repositories.PlayerAuthenticationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password4j.BcryptPassword4jPasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final PlayerAuthenticationRepository playerAuthenticationRepository;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BcryptPassword4jPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return playerAuthenticationRepository.findPlayerAuthenticationByUsername(username).orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));
            }
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration){
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider dao = new DaoAuthenticationProvider(userDetailsService());
        dao.setPasswordEncoder(passwordEncoder());
        return dao;
    }
}

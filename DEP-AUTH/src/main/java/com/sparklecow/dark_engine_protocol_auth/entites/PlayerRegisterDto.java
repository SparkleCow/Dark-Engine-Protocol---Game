package com.sparklecow.dark_engine_protocol_auth.entites;

import jakarta.validation.constraints.*;

public record PlayerRegisterDto(

        @NotBlank(message = "Username must not be blank")
        @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
        String username,

        @NotBlank(message = "Email must not be blank")
        @Email(message = "Email must be a valid email address")
        @Size(min = 6, max = 30, message = "Email must be between 6 and 30 characters")
        String email,

        @NotBlank(message = "Password must not be blank")
        @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
        String password
) {}
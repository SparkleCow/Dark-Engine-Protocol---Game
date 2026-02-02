package com.sparklecow.dark_engine_protocol.entities.dtos;

public record StatsEvent (
        String username,
        int level,
        Long experience,
        Long honor,
        Long companyPoints,
        Long credits,
        Long astato
) {}
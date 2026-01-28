package com.sparklecow.dark_engine_protocol.models;

public record MonsterDeathEvent(
        String monsterId,
        String killerPlayerId,
        int experience,
        int honor,
        int companyPoints,
        int credits,
        int astato
) {
}
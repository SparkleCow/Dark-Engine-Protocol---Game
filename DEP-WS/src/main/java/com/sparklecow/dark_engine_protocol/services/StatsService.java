package com.sparklecow.dark_engine_protocol.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsService {

    public void addExperience(
            String playerId,
            int experience,
            int honor,
            int companyPoints,
            int credits,
            int astato
    ) {
        log.info(
                "STATS UPDATE | playerId={} | xp={} | honor={} | companyPoints={} | credits={} | astato={}",
                playerId,
                experience,
                honor,
                companyPoints,
                credits,
                astato
        );
    }
}

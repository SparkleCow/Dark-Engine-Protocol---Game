package com.sparklecow.dark_engine_protocol.service;

import com.sparklecow.dark_engine_protocol.config.kafka.KafkaProducer;
import com.sparklecow.dark_engine_protocol.entities.Player;
import com.sparklecow.dark_engine_protocol.entities.Stats;
import com.sparklecow.dark_engine_protocol.entities.dtos.MonsterDeathEvent;
import com.sparklecow.dark_engine_protocol.entities.dtos.StatsEvent;
import com.sparklecow.dark_engine_protocol.repositories.PlayerRepository;
import com.sparklecow.dark_engine_protocol.repositories.StatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatsService {

    private static final int BASE_XP = 100;

    private final StatsRepository statsRepository;
    private final PlayerRepository playerRepository;
    private final KafkaProducerService kafkaProducerService;

    public void applyMonsterDeath(MonsterDeathEvent event) {

        Player player = playerRepository.findByUsername(event.killerPlayerId())
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Player not found: " + event.killerPlayerId()
                ));

        Stats stats = player.getStats();
        if (stats == null) {
            throw new IllegalStateException("Player has no stats initialized");
        }

        stats.setExperience(stats.getExperience() + event.experience());
        stats.setHonor(stats.getHonor() + event.honor());
        stats.setCompanyPoints(stats.getCompanyPoints() + event.companyPoints());
        stats.setCredits(stats.getCredits() + event.credits());
        stats.setAstato(stats.getAstato() + event.astato());

        int newLevel = calculateLevel(stats.getExperience());
        stats.setLevel(newLevel);

        Stats statsUpdated = statsRepository.save(stats);
        notifyStatsUpdated(statsUpdated, event.killerPlayerId());
    }

    private int calculateLevel(long totalXp) {
        int level = 1;

        while (totalXp >= xpRequiredForLevel(level + 1)) {
            level++;
        }

        return level;
    }

    private long xpRequiredForLevel(int level) {
        return (long) BASE_XP * level * level;
    }

    public void notifyStatsUpdated(Stats stats, String username){
        StatsEvent event = new StatsEvent(
                username,
                stats.getLevel(),
                stats.getExperience(),
                stats.getHonor(),
                stats.getCompanyPoints(),
                stats.getCredits(),
                stats.getAstato());

        kafkaProducerService.sendStats(event);
    }
}
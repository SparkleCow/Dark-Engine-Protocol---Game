package com.sparklecow.dark_engine_protocol.services;

import com.sparklecow.dark_engine_protocol.models.Monster;
import com.sparklecow.dark_engine_protocol.models.MonsterDeathEvent;
import com.sparklecow.dark_engine_protocol.models.MonsterSnapshot;
import com.sparklecow.dark_engine_protocol.models.MonsterType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class MonsterService {

    private final ApplicationEventPublisher eventPublisher;

    private final List<Monster> monsters = new ArrayList<>();
    public static final int MAP_WIDTH = 5000;
    public static final int MAP_HEIGHT = 5000;

    @PostConstruct
    public void init() {
        spawnInitialMonsters();
    }

    public void spawnInitialMonsters() {
        for (int i = 0; i < 30; i++) {
            monsters.add(new Monster(
                    MonsterType.SLIME,
                    1,
                    randomX(),
                    randomY()
            ));
        }
    }

    //Random initial position
    public double randomX(){
        return ThreadLocalRandom.current().nextDouble(0, MAP_WIDTH);
    }
    public double randomY(){
        return ThreadLocalRandom.current().nextDouble(0, MAP_HEIGHT);
    }

    public void updateAll(long now) {
        for (Monster monster : monsters) {
            monster.update(now);
        }
    }

    //Mapper
    public List<MonsterSnapshot> getMonsterSnapshots() {
        return monsters.stream()
                .filter(Monster::isAlive)
                .map(m -> new MonsterSnapshot(
                        m.getId(),
                        m.getType().name(),
                        m.getX(),
                        m.getY(),
                        m.getHp(),
                        m.getMaxHp(),
                        true
                ))
                .toList();
    }

    public void handleMonsterDeath(Monster monster, String killerPlayerId) {

        int xp = monster.getType().getReward().getExperience();
        int honor = monster.getType().getReward().getHonor();
        int credits = monster.getType().getReward().getCredits();
        int company = monster.getType().getReward().getCompanyPoints();
        int astato = monster.getType().getReward().getAstato();

        MonsterDeathEvent event = new MonsterDeathEvent(
                monster.getId(),
                killerPlayerId,
                xp,
                honor,
                company,
                credits,
                astato
        );

        eventPublisher.publishEvent(event);
    }

    public Monster getMonsterById(String id) {
        return monsters.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

}
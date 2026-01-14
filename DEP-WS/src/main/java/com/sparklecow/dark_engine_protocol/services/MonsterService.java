package com.sparklecow.dark_engine_protocol.services;

import com.sparklecow.dark_engine_protocol.models.Monster;
import com.sparklecow.dark_engine_protocol.models.MonsterSnapshot;
import com.sparklecow.dark_engine_protocol.models.MonsterType;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class MonsterService {

    private final List<Monster> monsters = new ArrayList<>();
    public static final int MAP_WIDTH = 5000;
    public static final int MAP_HEIGHT = 5000;

    @PostConstruct
    public void init() {
        spawnInitialMonsters();
    }

    public void spawnInitialMonsters() {
        for (int i = 0; i < 20; i++) {
            monsters.add(new Monster(
                    MonsterType.SLIME,
                    1,
                    randomX(),
                    randomY()
            ));
        }
    }

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

    public List<MonsterSnapshot> getMonsterSnapshots() {
        return monsters.stream()
                .filter(Monster::isAlive)
                .map(m -> new MonsterSnapshot(
                        m.getId(),
                        m.getType().name(),
                        m.getX(),
                        m.getY(),
                        m.getHp(),
                        true
                ))
                .toList();
    }
}
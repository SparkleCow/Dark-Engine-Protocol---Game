package com.sparklecow.dark_engine_protocol.services;

import com.sparklecow.dark_engine_protocol.models.Monster;
import com.sparklecow.dark_engine_protocol.models.Position;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CombatService {

    private static final int BASIC_DAMAGE = 10;
    private static final double ATTACK_RANGE = 500;

    private final MonsterService monsterService;
    private final PositionService positionService;

    public void attackMonster(String username, String id){
        Monster monster = monsterService.getMonsterById(id);
        if (monster == null || !monster.isAlive()) return;

        Position playerPos = positionService.getPosition(username);
        if (playerPos == null) return;

        if (!isInRange(playerPos, monster)) return;

        monster.receiveDamage(BASIC_DAMAGE, username);

        if (!monster.isAlive()) {
            monsterService.handleMonsterDeath(monster, username);
        }
    }

    private boolean isInRange(Position p, Monster m) {
        double dx = p.getX() - m.getX();
        double dy = p.getY() - m.getY();
        return Math.sqrt(dx * dx + dy * dy) <= ATTACK_RANGE;
    }
}

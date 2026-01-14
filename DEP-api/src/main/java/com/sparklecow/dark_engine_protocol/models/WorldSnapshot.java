package com.sparklecow.dark_engine_protocol.models;

import com.sparklecow.dark_engine_protocol.entities.Position;

import java.util.List;

public class WorldSnapshot {

    private long timestamp;
    private List<Position> players;
    private List<MonsterSnapshot> monsters;

    public WorldSnapshot(List<Position> players,
                         List<MonsterSnapshot> monsters) {
        this.timestamp = System.currentTimeMillis();
        this.players = players;
        this.monsters = monsters;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public List<Position> getPlayers() {
        return players;
    }

    public List<MonsterSnapshot> getMonsters() {
        return monsters;
    }
}

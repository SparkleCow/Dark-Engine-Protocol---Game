package com.sparklecow.dark_engine_protocol.models;

/*This model will send monsters and players in the world*/
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

package com.sparklecow.dark_engine_protocol.models;

import lombok.Setter;

import java.util.UUID;

public class Monster {

    private final String id;
    private final MonsterType type;

    private int mapId;
    private double x;
    private double y;

    private double dirX;
    private double dirY;

    private double speed;

    private int maxHp;
    private int hp;
    private boolean alive;

    private long nextDirectionChangeAt; // timestamp ms
    private long directionChangeIntervalMs;

    private Reward reward;
    private String lastDamagedByPlayerId;


    public Monster(MonsterType type, int mapId, double x, double y) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.mapId = mapId;
        this.x = x;
        this.y = y;

        this.speed = type.getBaseSpeed();

        this.maxHp = type.getBaseHp();
        this.hp = maxHp;
        this.alive = true;
        this.lastDamagedByPlayerId = null;

        this.directionChangeIntervalMs = type.getDirectionChangeIntervalMs();
        scheduleNextDirectionChange();

        randomizeDirection();

        this.reward = type.getReward();
    }

    public void update(long now) {
        if (!alive) return;

        // Do we have to change direction?
        if (now >= nextDirectionChangeAt) {
            randomizeDirection();
            scheduleNextDirectionChange();
        }

        // Movement
        this.x += dirX * speed;
        this.y += dirY * speed;

        clampMonster(5000, 5000);
    }

    // This method limits monster movement to the map
    public void clampMonster(double WORLD_WIDTH, double WORLD_HEIGHT){
        this.x = Math.max(
                0,
                Math.min(WORLD_WIDTH, this.x)
        );
        this.y = Math.max(
                0,
                Math.min(WORLD_HEIGHT, this.y)
        );
    }

    private void randomizeDirection() {
        double angle = Math.random() * Math.PI * 2;
        this.dirX = Math.cos(angle);
        this.dirY = Math.sin(angle);
    }

    private void scheduleNextDirectionChange() {
        this.nextDirectionChangeAt =
                System.currentTimeMillis() + directionChangeIntervalMs;
    }

    // Combat
    public void receiveDamage(int damage, String playerId) {
        if (!alive) return;

        hp -= damage;
        this.lastDamagedByPlayerId = playerId;

        if (hp <= 0) {
            hp = 0;
            alive = false;
        }
    }

    public String getId() { return id; }
    public MonsterType getType() { return type; }
    public int getMapId() { return mapId; }
    public double getX() { return x; }
    public double getY() { return y; }
    public int getHp() { return hp; }
    public int getMaxHp() {return maxHp;}
    public boolean isAlive() { return alive; }
    public Reward getReward() { return reward; }
}

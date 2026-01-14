package com.sparklecow.dark_engine_protocol.models;

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

        this.directionChangeIntervalMs = type.getDirectionChangeIntervalMs();
        scheduleNextDirectionChange();

        randomizeDirection();

        this.reward = type.getReward();
    }

    // -----------------------
    // Lógica de movimiento
    // -----------------------
    public void update(long now) {
        if (!alive) return;

        // ¿Es hora de cambiar dirección?
        if (now >= nextDirectionChangeAt) {
            randomizeDirection();
            scheduleNextDirectionChange();
        }

        // Movimiento
        this.x += dirX * speed;
        this.y += dirY * speed;
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

    // -----------------------
    // Combate
    // -----------------------
    public void receiveDamage(int damage) {
        if (!alive) return;

        hp -= damage;
        if (hp <= 0) {
            hp = 0;
            alive = false;
        }
    }

    // -----------------------
    // Getters (sin setters peligrosos)
    // -----------------------
    public String getId() { return id; }
    public MonsterType getType() { return type; }
    public int getMapId() { return mapId; }
    public double getX() { return x; }
    public double getY() { return y; }
    public int getHp() { return hp; }
    public boolean isAlive() { return alive; }
    public Reward getReward() { return reward; }
}

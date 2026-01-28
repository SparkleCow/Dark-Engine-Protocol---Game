package com.sparklecow.dark_engine_protocol.models;

public class MonsterSnapshot {

    private String id;
    private String type;
    private double x;
    private double y;
    private int hp;
    private int maxHp;
    private boolean alive;

    public MonsterSnapshot(String id,
                           String type,
                           double x,
                           double y,
                           int hp,
                           int maxHp,
                           boolean alive) {
        this.id = id;
        this.type = type;
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.maxHp = maxHp;
        this.alive = alive;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp(){
        return maxHp;
    }

    public boolean isAlive() {
        return alive;
    }
}

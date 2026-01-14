package com.sparklecow.dark_engine_protocol.models;

public enum MonsterType {

    SLIME(
            50,
            1.2,
            3000,
            new Reward(10, 5)
    ),

    DRONE(
            80,
            2.5,
            2000,
            new Reward(20, 10)
    ),

    SENTINEL(
            200,
            0.8,
            5000,
            new Reward(100, 50)
    );

    private final int baseHp;
    private final double baseSpeed;
    private final long directionChangeIntervalMs;
    private final Reward reward;

    MonsterType(int baseHp,
                double baseSpeed,
                long directionChangeIntervalMs,
                Reward reward) {
        this.baseHp = baseHp;
        this.baseSpeed = baseSpeed;
        this.directionChangeIntervalMs = directionChangeIntervalMs;
        this.reward = reward;
    }

    public int getBaseHp() { return baseHp; }
    public double getBaseSpeed() { return baseSpeed; }
    public long getDirectionChangeIntervalMs() { return directionChangeIntervalMs; }
    public Reward getReward() { return reward; }
}

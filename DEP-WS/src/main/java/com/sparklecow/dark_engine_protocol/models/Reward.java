package com.sparklecow.dark_engine_protocol.models;

public class Reward {

    private int experience;
    private int credits;

    public Reward(int experience, int credits) {
        this.experience = experience;
        this.credits = credits;
    }

    public int getExperience() {
        return experience;
    }

    public int getCredits() {
        return credits;
    }
}

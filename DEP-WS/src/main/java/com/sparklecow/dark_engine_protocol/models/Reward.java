package com.sparklecow.dark_engine_protocol.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class Reward {

    private int experience;
    private int credits;
    private int honor;
    private int companyPoints;
    private int astato;

    public Reward(int experience, int credits, int honor, int companyPoints, int astato) {
        this.experience = experience;
        this.credits = credits;
        this.honor = honor;
        this.companyPoints = companyPoints;
        this.astato = astato;
    }

    public int getExperience() {
        return experience;
    }

    public int getCredits() {
        return credits;
    }
}

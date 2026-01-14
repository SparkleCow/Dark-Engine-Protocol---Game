package com.sparklecow.dark_engine_protocol.entities;

import lombok.Getter;

@Getter
public enum Level {
    BASE(1);

    private Integer level;

    Level(Integer level){
        this.level = level;
    }
}

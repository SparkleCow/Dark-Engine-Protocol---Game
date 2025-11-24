package com.sparklecow.dark_engine_protocol.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "position")
public class Position {

    private Long xPosition;

    private Long yPosition;

    private Level level;
}

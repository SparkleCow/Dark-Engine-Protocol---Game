package com.sparklecow.dark_engine_protocol.entities;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Position implements Serializable {

    private Long playerId;

    private double x;

    private double y;

    private int mapId;

    private double angle;
}
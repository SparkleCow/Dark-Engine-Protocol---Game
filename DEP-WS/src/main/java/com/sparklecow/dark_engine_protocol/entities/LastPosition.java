package com.sparklecow.dark_engine_protocol.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "last_position")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LastPosition {

    @Id
    private Long playerId;
    private double x;
    private double y;
    private int mapId;
    private double angle;
}
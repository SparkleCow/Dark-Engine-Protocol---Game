package com.sparklecow.dark_engine_protocol.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stats")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Progress
    private Integer level;
    private Long experience;

    // Reputation / honor
    private Long honor;

    // Economy
    private Long companyPoints;
    private Long credits;
    private Long astato;
}
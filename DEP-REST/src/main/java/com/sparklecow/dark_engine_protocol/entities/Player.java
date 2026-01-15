package com.sparklecow.dark_engine_protocol.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "players")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Player{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true)
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "stats_id")
    private Stats stats;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "position_id")
    private LastPosition lastPosition;
}

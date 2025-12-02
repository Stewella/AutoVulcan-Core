package com.prosigmaka.catra.diglett.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "t_status_available")
public class StatusAvailable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "grup_available")
    private String grupAvailable;

    @Column(name = "status")
    private String status;
}

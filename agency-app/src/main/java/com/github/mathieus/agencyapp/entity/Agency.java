package com.github.mathieus.agencyapp.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "agency", schema = "agency")
public class Agency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "national_id", nullable = false, unique = true, length = 50)
    private String nationalId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNationalId() { return nationalId; }
    public void setNationalId(String nationalId) { this.nationalId = nationalId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
package com.github.mathieus.loanbackoffice.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "agency")
public class Agency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "national_id", nullable = false, unique = true, length = 50)
    private String nationalId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "agency", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<CreditApplication> creditApplications;

    public Agency() {}

    public Agency(String nationalId, String name) {
        this.nationalId = nationalId;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CreditApplication> getCreditApplications() {
        return creditApplications;
    }

    public void setCreditApplications(List<CreditApplication> creditApplications) {
        this.creditApplications = creditApplications;
    }
}

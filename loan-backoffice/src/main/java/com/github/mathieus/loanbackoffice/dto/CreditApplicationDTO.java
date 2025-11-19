package com.github.mathieus.loanbackoffice.dto;


import com.github.mathieus.loanbackoffice.enums.Status;

import java.math.BigDecimal;

public class CreditApplicationDTO {

    private Long id;
    private String applicationNumber;
    private String applicantName;
    private String applicantEmail;
    private BigDecimal amount;
    private Integer termMonths;
    private String purpose;
    private String agencyNationalId;
    private Status status;

    // Constructors
    public CreditApplicationDTO() {}

    public CreditApplicationDTO(Long id, String applicationNumber, String applicantName,
                                String applicantEmail, BigDecimal amount, Integer termMonths,
                                String purpose, String agencyNationalId, Status status) {
        this.id = id;
        this.applicationNumber = applicationNumber;
        this.applicantName = applicantName;
        this.applicantEmail = applicantEmail;
        this.amount = amount;
        this.termMonths = termMonths;
        this.purpose = purpose;
        this.agencyNationalId = agencyNationalId;
        this.status = status;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getApplicationNumber() { return applicationNumber; }
    public void setApplicationNumber(String applicationNumber) { this.applicationNumber = applicationNumber; }

    public String getApplicantName() { return applicantName; }
    public void setApplicantName(String applicantName) { this.applicantName = applicantName; }

    public String getApplicantEmail() { return applicantEmail; }
    public void setApplicantEmail(String applicantEmail) { this.applicantEmail = applicantEmail; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public Integer getTermMonths() { return termMonths; }
    public void setTermMonths(Integer termMonths) { this.termMonths = termMonths; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public String getAgencyNationalId() { return agencyNationalId; }
    public void setAgencyNationalId(String agencyNationalId) { this.agencyNationalId = agencyNationalId; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}


package com.github.mathieus.syncer.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.time.Instant;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostgresCreditApplicationDTO {

    private Integer id;
    private String applicationNumber;
    private String applicantName;
    private String applicantEmail;
    private BigDecimal amount;
    private Integer termMonths;
    private String purpose;
    private String agencyNationalId;
    private String status;
    private Instant originLastUpdate;

    public static PostgresCreditApplicationDTO of(MysqlCreditApplicationDTO dto, String nationalId) {
        PostgresCreditApplicationDTO target = new PostgresCreditApplicationDTO();
        target.setId(dto.getId());
        target.setApplicationNumber(dto.getApplicationNumber());
        target.setApplicantName(dto.getApplicantName());
        target.setApplicantEmail(dto.getApplicantEmail());
        target.setAmount(dto.getAmount());
        target.setTermMonths(dto.getTermMonths());
        target.setPurpose(dto.getPurpose());
        target.setAgencyNationalId(nationalId);
        target.setStatus(dto.getStatus());
        target.setOriginLastUpdate(dto.getLastUpdate());
        return target;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantEmail() {
        return applicantEmail;
    }

    public void setApplicantEmail(String applicantEmail) {
        this.applicantEmail = applicantEmail;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getTermMonths() {
        return termMonths;
    }

    public void setTermMonths(Integer termMonths) {
        this.termMonths = termMonths;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getAgencyNationalId() {
        return agencyNationalId;
    }

    public void setAgencyNationalId(String agencyNationalId) {
        this.agencyNationalId = agencyNationalId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getOriginLastUpdate() {
        return originLastUpdate;
    }

    public void setOriginLastUpdate(Instant originLastUpdate) {
        this.originLastUpdate = originLastUpdate;
    }
}
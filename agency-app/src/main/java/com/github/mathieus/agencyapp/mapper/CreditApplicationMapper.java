package com.github.mathieus.agencyapp.mapper;


import com.github.mathieus.agencyapp.dto.CreditApplicationDTO;
import com.github.mathieus.agencyapp.entity.CreditApplication;

public class CreditApplicationMapper {

    public static CreditApplicationDTO toDTO(CreditApplication entity) {
        if (entity == null) return null;
        return new CreditApplicationDTO(
                entity.getId(),
                entity.getApplicationNumber(),
                entity.getApplicantName(),
                entity.getApplicantEmail(),
                entity.getAmount(),
                entity.getTermMonths(),
                entity.getPurpose(),
                entity.getAgencyNationalId(),
                entity.getStatus()
        );
    }

    public static void updateEntity(CreditApplication entity, CreditApplicationDTO dto) {
        if (dto.getApplicationNumber() != null) entity.setApplicationNumber(dto.getApplicationNumber());
        if (dto.getApplicantName() != null) entity.setApplicantName(dto.getApplicantName());
        if (dto.getApplicantEmail() != null) entity.setApplicantEmail(dto.getApplicantEmail());
        if (dto.getAmount() != null) entity.setAmount(dto.getAmount());
        if (dto.getTermMonths() != null) entity.setTermMonths(dto.getTermMonths());
        if (dto.getPurpose() != null) entity.setPurpose(dto.getPurpose());
        if (dto.getStatus() != null) entity.setStatus(dto.getStatus());
    }
}
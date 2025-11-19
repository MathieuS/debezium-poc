package com.github.mathieus.loanbackoffice.controller;

import com.github.mathieus.loanbackoffice.dto.CreditApplicationDTO;
import com.github.mathieus.loanbackoffice.entity.Agency;
import com.github.mathieus.loanbackoffice.entity.CreditApplication;
import com.github.mathieus.loanbackoffice.mapper.CreditApplicationMapper;
import com.github.mathieus.loanbackoffice.repository.AgencyRepository;
import com.github.mathieus.loanbackoffice.repository.CreditApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/credit-applications")
public class CreditApplicationController {

    @Autowired
    private CreditApplicationRepository creditApplicationRepository;

    @Autowired
    private AgencyRepository agencyRepository;

    @GetMapping("/{id}")
    public ResponseEntity<CreditApplicationDTO> getById(@PathVariable Long id) {
        Optional<CreditApplication> creditApp = creditApplicationRepository.findById(id);
        return creditApp.map(app -> ResponseEntity.ok(CreditApplicationMapper.toDTO(app)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<CreditApplicationDTO>> getByAgency(
            @RequestParam Long agencyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Optional<Agency> agencyOpt = agencyRepository.findById(agencyId);
        if (agencyOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CreditApplicationDTO> creditApps = creditApplicationRepository
                .findByAgency(agencyOpt.get(), pageable)
                .map(CreditApplicationMapper::toDTO);

        return ResponseEntity.ok(creditApps);
    }

    @PostMapping
    public ResponseEntity<CreditApplicationDTO> create(@RequestBody CreditApplicationDTO dto) {
        if (dto.getAgencyNationalId() == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Agency> agencyOpt = agencyRepository.findByNationalId(dto.getAgencyNationalId()) != null
                ? Optional.ofNullable(agencyRepository.findByNationalId(dto.getAgencyNationalId()))
                : Optional.empty();

        if (agencyOpt.isEmpty()) return ResponseEntity.badRequest().build();

        CreditApplication entity = new CreditApplication();
        CreditApplicationMapper.updateEntity(entity, dto);
        entity.setAgency(agencyOpt.get());

        CreditApplication saved = creditApplicationRepository.save(entity);
        return ResponseEntity.ok(CreditApplicationMapper.toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreditApplicationDTO> update(
            @PathVariable Long id,
            @RequestBody CreditApplicationDTO dto
    ) {
        Optional<CreditApplication> existingOpt = creditApplicationRepository.findById(id);
        if (existingOpt.isEmpty()) return ResponseEntity.notFound().build();

        CreditApplication existing = existingOpt.get();
        CreditApplicationMapper.updateEntity(existing, dto);

        if (dto.getAgencyNationalId() != null) {
            Optional<Agency> agencyOpt = Optional.ofNullable(agencyRepository.findByNationalId(dto.getAgencyNationalId()));
            agencyOpt.ifPresent(existing::setAgency);
        }

        CreditApplication updated = creditApplicationRepository.save(existing);
        return ResponseEntity.ok(CreditApplicationMapper.toDTO(updated));
    }
}


package com.github.mathieus.agencyapp.controller;

import com.github.mathieus.agencyapp.adapter.BackofficeRestClient;
import com.github.mathieus.agencyapp.dto.CreditApplicationDTO;
import com.github.mathieus.agencyapp.dto.PageResponse;
import com.github.mathieus.agencyapp.entity.Agency;
import com.github.mathieus.agencyapp.entity.CreditApplication;
import com.github.mathieus.agencyapp.mapper.CreditApplicationMapper;
import com.github.mathieus.agencyapp.repository.AgencyRepository;
import com.github.mathieus.agencyapp.repository.CreditApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private BackofficeRestClient backofficeRestClient;

    @Value("${agency-app.use-local-database:false}")
    private boolean useLocalDatabase;

    @GetMapping("/{id}")
    public ResponseEntity<CreditApplicationDTO> getById(@PathVariable Long id) {
        if(!useLocalDatabase){
            return ResponseEntity.ok(backofficeRestClient.getById(id));
        }else {
            Optional<CreditApplication> creditApp = creditApplicationRepository.findById(id);
            return creditApp.map(app -> ResponseEntity.ok(CreditApplicationMapper.toDTO(app)))
                    .orElse(ResponseEntity.notFound().build());
        }
    }

    @GetMapping
    public ResponseEntity<Page<CreditApplicationDTO>> getByAgency(
            @RequestParam Long agencyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {

        if(!useLocalDatabase){
            return ResponseEntity.ok(backofficeRestClient.getByAgency(agencyId, page, size, sortBy, sortDir));
        } else {
            Optional<Agency> agencyOpt = agencyRepository.findById(agencyId);
            if (agencyOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<CreditApplicationDTO> creditApps = creditApplicationRepository
                    .findByAgencyNationalId(agencyOpt.get().getNationalId(), pageable)
                    .map(CreditApplicationMapper::toDTO);
            return ResponseEntity.ok(creditApps);
        }

    }

    @PostMapping
    public ResponseEntity<CreditApplicationDTO> create(@RequestBody CreditApplicationDTO dto) {
        return ResponseEntity.ok(backofficeRestClient.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreditApplicationDTO> update(
            @PathVariable Long id,
            @RequestBody CreditApplicationDTO dto
    ) {
        return backofficeRestClient.update(id, dto);
    }
}


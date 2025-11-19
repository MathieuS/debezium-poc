package com.github.mathieus.agencyapp.repository;

import com.github.mathieus.agencyapp.entity.Agency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgencyRepository extends JpaRepository<Agency, Long> {

    Agency findByNationalId(String nationalId);
}
package com.github.mathieus.loanbackoffice.repository;

import com.github.mathieus.loanbackoffice.entity.Agency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgencyRepository extends JpaRepository<Agency, Long> {

    Agency findByNationalId(String nationalId);
}
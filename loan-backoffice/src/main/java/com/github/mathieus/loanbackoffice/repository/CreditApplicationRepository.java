package com.github.mathieus.loanbackoffice.repository;

import com.github.mathieus.loanbackoffice.entity.Agency;
import com.github.mathieus.loanbackoffice.entity.CreditApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditApplicationRepository extends JpaRepository<CreditApplication, Long> {

    Page<CreditApplication> findByAgency(Agency agency, Pageable pageable);

}

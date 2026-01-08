package com.github.mathieus.syncer.service;

import com.github.mathieus.syncer.dto.PostgresCreditApplicationDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class PostgresService {

    private final JdbcTemplate postgresJdbcTemplate;

    public PostgresService(@Qualifier("postgres") JdbcTemplate postgresJdbc) {
        this.postgresJdbcTemplate = postgresJdbc;
    }

    private static final String UPSERT_SQL = """
            INSERT INTO backoffice_view.credit_application (
                id,
                application_number,
                applicant_name,
                applicant_email,
                amount,
                term_months,
                purpose,
                agency_national_id,
                status,
                origin_last_update,
                last_update
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, CAST(? AS backoffice_view.status_enum), ?, NOW())
            ON CONFLICT (id) DO UPDATE SET
                application_number = EXCLUDED.application_number,
                applicant_name = EXCLUDED.applicant_name,
                applicant_email = EXCLUDED.applicant_email,
                amount = EXCLUDED.amount,
                term_months = EXCLUDED.term_months,
                purpose = EXCLUDED.purpose,
                agency_national_id = EXCLUDED.agency_national_id,
                status = EXCLUDED.status,
                origin_last_update = EXCLUDED.origin_last_update,
                last_update = EXCLUDED.last_update;
            """;

    public void upsert(PostgresCreditApplicationDTO dto) {
        postgresJdbcTemplate.update(
                UPSERT_SQL,
                dto.getId(),
                dto.getApplicationNumber(),
                dto.getApplicantName(),
                dto.getApplicantEmail(),
                dto.getAmount(),
                dto.getTermMonths(),
                dto.getPurpose(),
                dto.getAgencyNationalId(),
                dto.getStatus(),
                dto.getOriginLastUpdate() != null ?
                        Timestamp.from(dto.getOriginLastUpdate()) : null
        );
    }
}
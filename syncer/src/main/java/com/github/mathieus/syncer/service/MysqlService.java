package com.github.mathieus.syncer.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class MysqlService {

    private final JdbcTemplate mysqlJdbc;

    public MysqlService(@Qualifier("mysql") JdbcTemplate mysqlJdbc) {
        this.mysqlJdbc = mysqlJdbc;
    }

    public String getNationalId(Integer agencyId) {

        return  mysqlJdbc.queryForObject(
                "SELECT national_id FROM agency WHERE id = ?",
                String.class,
                agencyId
        );

    }
}

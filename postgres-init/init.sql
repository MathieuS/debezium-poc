CREATE SCHEMA IF NOT EXISTS agency;
CREATE SCHEMA IF NOT EXISTS backoffice_view;

CREATE TYPE backoffice_view.status_enum AS ENUM ('PENDING', 'IN_REVIEW', 'APPROVED', 'REJECTED');

CREATE TABLE agency.agency (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    national_id VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL
);


CREATE TABLE backoffice_view.credit_application (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    application_number VARCHAR(50) NOT NULL UNIQUE,
    applicant_name VARCHAR(100) NOT NULL,
    applicant_email VARCHAR(100) NOT NULL,
    amount NUMERIC(12,2) NOT NULL,
    term_months INT NOT NULL,
    purpose VARCHAR(255),
    agency_national_id VARCHAR(50) NOT NULL,
    status backoffice_view.status_enum DEFAULT 'PENDING',
    origin_last_update TIMESTAMP,
    last_update TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO agency.agency (national_id, name)
SELECT
    'AG' || LPAD(gs::text, 4, '0') AS national_id,
    'Agency ' || gs AS name
FROM generate_series(1, 2000) AS gs;

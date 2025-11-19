CREATE TABLE agency (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    national_id VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE credit_application (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    application_number VARCHAR(50) NOT NULL UNIQUE,
    applicant_name VARCHAR(100) NOT NULL,
    applicant_email VARCHAR(100) NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    term_months INT NOT NULL,
    purpose VARCHAR(255),
    agency_id BIGINT NOT NULL,
    status ENUM('PENDING', 'IN_REVIEW', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    last_update TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_credit_application_agency FOREIGN KEY (agency_id) REFERENCES agency(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


INSERT INTO agency (national_id, name)
SELECT
    CONCAT('AG', LPAD(t.n, 4, '0')) AS national_id,
    CONCAT('Agency ', t.n) AS name
FROM (
    SELECT @rownum := @rownum + 1 AS n
    FROM information_schema.columns, (SELECT @rownum := 0) r
    LIMIT 2000
) t;

INSERT INTO credit_application (
    application_number, applicant_name, applicant_email,
    amount, term_months, purpose, agency_id, status
)
SELECT
    CONCAT('APP', LPAD(t.n, 6, '0')) AS application_number,
    CONCAT('Applicant ', t.n) AS applicant_name,
    CONCAT('applicant', t.n, '@example.com') AS applicant_email,
    ROUND(RAND() * 40000 + 1000, 2) AS amount,
    FLOOR(RAND() * 48 + 12) AS term_months,
    ELT(FLOOR(1 + RAND() * 5),
        'Car loan', 'Home improvement', 'Education', 'Vacation', 'Business') AS purpose,
    FLOOR(1 + RAND() * 2000) AS agency_id,
    ELT(FLOOR(1 + RAND() * 4),
        'PENDING', 'IN_REVIEW', 'APPROVED', 'REJECTED') AS status
FROM (
    SELECT @rownum := @rownum + 1 AS n
    FROM information_schema.columns, information_schema.tables, (SELECT @rownum := 0) r
    LIMIT 100000
) t;

GRANT REPLICATION CLIENT ON *.* TO 'demo'@'%';
GRANT REPLICATION SLAVE ON *.* TO 'demo'@'%';
FLUSH PRIVILEGES;
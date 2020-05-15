-----------------------------------------------------
-- TEMPLATES TABLE
-----------------------------------------------------
CREATE TABLE template
(
    domain_id  VARCHAR(50)  NOT NULL,
    event_UUID UUID         NOT NULL,
    event_type VARCHAR(200) NOT NULL,
    event_time BIGINT       NOT NULL,
    event_body JSON,
    PRIMARY KEY (domain_id, event_UUID)
);

ALTER TABLE template
    OWNER TO ${owner};

CREATE INDEX IDX_TEMPLATE
    ON template (domain_id, event_UUID);

-----------------------------------------------------
-- JOBS TABLE
-----------------------------------------------------
CREATE TABLE job
(
    domain_id  UUID         NOT NULL,
    event_UUID UUID         NOT NULL,
    event_type VARCHAR(200) NOT NULL,
    event_time BIGINT       NOT NULL,
    event_body JSON,
    PRIMARY KEY (domain_id, event_UUID)
);

ALTER TABLE job
    OWNER TO ${owner};

CREATE INDEX IDX_JOB
    ON job (domain_id, event_UUID);

-----------------------------------------------------
-- JOBS TABLE
-----------------------------------------------------
CREATE TABLE job_qstat
(
    id        UUID        NOT NULL,
    qstat     TEXT        NOT NULL,
    job_group VARCHAR(50),
    status    VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE job_qstat
    OWNER TO ${owner};

CREATE INDEX IDX_QSTAT
    ON job_qstat (id);
-----------------------------------------------------
-- UPLOADS TABLE
-----------------------------------------------------
CREATE TABLE upload
(
    domain_id  UUID         NOT NULL,
    event_UUID UUID         NOT NULL,
    event_type VARCHAR(200) NOT NULL,
    event_time BIGINT       NOT NULL,
    event_body JSON,
    PRIMARY KEY (domain_id, event_UUID)
);

ALTER TABLE upload
    OWNER TO ${owner};

CREATE INDEX IDX_UPLOAD
    ON upload (domain_id, event_UUID);


-----------------------------------------------------
-- UPLOAD STATUSES TABLE
-----------------------------------------------------
CREATE TABLE upload_status
(
    id         UUID    NOT NULL,
    start_date BIGINT  NOT NULL,
    notified   BOOLEAN NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE upload_status
    OWNER TO ${owner};

CREATE INDEX IDX_UPL_STAT
    ON upload_status (id);

CREATE TABLE file_upload_status
(
    id               UUID         NOT NULL,
    upload_status_id UUID         NOT NULL,
    filename         VARCHAR(100) NOT NULL,
    error            TEXT,
    PRIMARY KEY (id, upload_status_id)
);

ALTER TABLE file_upload_status
    OWNER TO ${owner};

CREATE INDEX IDX_FILE_UPL_STAT
    ON file_upload_status (id, upload_status_id);
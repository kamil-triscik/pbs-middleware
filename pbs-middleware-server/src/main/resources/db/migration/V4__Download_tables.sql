-----------------------------------------------------
-- DOWNLOADS TABLE
-----------------------------------------------------
CREATE TABLE download
(
    domain_id  UUID         NOT NULL,
    event_UUID UUID         NOT NULL,
    event_type VARCHAR(200) NOT NULL,
    event_time BIGINT       NOT NULL,
    event_body JSON,
    PRIMARY KEY (domain_id, event_UUID)
);

ALTER TABLE download
    OWNER TO ${owner};

CREATE INDEX IDX_DOWNLOAD
    ON download (domain_id, event_UUID);

-----------------------------------------------------
-- DOWNLOAD STATUSES TABLE
-----------------------------------------------------

CREATE TABLE download_status
(
    id         UUID    NOT NULL,
    start_date BIGINT  NOT NULL,
    notified   BOOLEAN NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE download_status
    OWNER TO ${owner};

CREATE INDEX IDX_DOWN_STAT
    ON download_status (id);

CREATE TABLE file_download_status
(
    id                 UUID         NOT NULL,
    download_status_id UUID         NOT NULL,
    filename           VARCHAR(100) NOT NULL,
    error              TEXT,
    PRIMARY KEY (id, download_status_id)
);

ALTER TABLE file_download_status
    OWNER TO ${owner};

CREATE INDEX IDX_FILE_DOWN_STAT
    ON file_download_status (id, download_status_id);
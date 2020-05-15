-----------------------------------------------------
-- USERS TABLE
-----------------------------------------------------
CREATE TABLE "user"
(
    id          UUID,
    username    VARCHAR(16)  NOT NULL UNIQUE,
    password    VARCHAR(72)  NOT NULL,
    first_name  VARCHAR(32)  NOT NULL,
    last_name   VARCHAR(32)  NOT NULL,
    email       VARCHAR(256) NOT NULL,
    is_disabled BOOLEAN,
    user_role   VARCHAR(15)  NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE "user"
    OWNER TO ${owner};

CREATE INDEX IDX_USER_EMAIL
    ON "user" (email);

CREATE INDEX IDX_USER_NAME
    ON "user" (username);

-----------------------------------------------------
-- OWNERSHIP TABLE
-----------------------------------------------------
CREATE TABLE ownership
(
    domain_id   UUID        NOT NULL,
    owner_id    UUID        NOT NULL,
    domain_type VARCHAR(20) NOT NULL,
    PRIMARY KEY (domain_id, owner_id)
);

ALTER TABLE ownership
    OWNER TO ${owner};

CREATE INDEX IDX_OWNERSHIP
    ON ownership (domain_id, owner_id);

-----------------------------------------------------
-- CONNECTIONS TABLE
-----------------------------------------------------

CREATE TABLE connection
(
    id          UUID         NOT NULL,
    name        VARCHAR(20)  NOT NULL UNIQUE,
    description VARCHAR(100),
    pbs_host    VARCHAR(100) NOT NULL,
    ssh_host    VARCHAR(100),
    login       VARCHAR(50),
    password    VARCHAR(50),
    ssh_key     VARCHAR(300),
    type        VARCHAR(20)  NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE connection
    OWNER TO ${owner};

CREATE INDEX IDX_CONN_ID
    ON connection (id);

CREATE INDEX IDX_CONN_NAME
    ON connection (name);

-----------------------------------------------------
-- SCRIPTS TABLE
-----------------------------------------------------
CREATE TABLE script
(
    id          UUID        NOT NULL,
    name        VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(100),
    code        TEXT        NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE script
    OWNER TO ${owner};

CREATE INDEX IDX_SCRIPT_ID
    ON script (id);

-----------------------------------------------------
-- CONTACTS TABLE
-----------------------------------------------------
CREATE TABLE contact
(
    id          UUID        NOT NULL,
    email       VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(100),
    PRIMARY KEY (id)
);

ALTER TABLE contact
    OWNER TO ${owner};

CREATE INDEX IDX_CONTACT
    ON contact (id);

CREATE TABLE contact_type
(

    contact_id   UUID        NOT NULL,
    contact_type VARCHAR(10) NOT NULL,
    FOREIGN KEY (contact_id) REFERENCES contact (id)
);

-----------------------------------------------------
-- TEMPORARY DATA TABLE
-----------------------------------------------------
CREATE TABLE temporary_data
(
    id        TEXT   NOT NULL,
    domain_id UUID   NOT NULL,
    size      BIGINT NOT NULL,
    hash      TEXT   NOT NULL,
    data      BYTEA  NOT NULL,
    PRIMARY KEY (id, domain_id)
);

ALTER TABLE temporary_data
    OWNER TO ${owner};

CREATE INDEX IDX_TEMP
    ON temporary_data (id, domain_id);

-----------------------------------------------------
-- EMAILS TABLE
-----------------------------------------------------
CREATE TABLE email
(
    id          UUID         NOT NULL,
    recipient   VARCHAR(150) NOT NULL,
    subject     VARCHAR(150) NOT NULL,
    body        VARCHAR      NOT NULL,
    state       VARCHAR(15)  NOT NULL,
    create_date BIGINT       NOT NULL,
    send_date   BIGINT,
    PRIMARY KEY (id)
);

ALTER TABLE email
    OWNER TO ${owner};

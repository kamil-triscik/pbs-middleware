CREATE TABLE customer (
                      domain_id VARCHAR(50) NOT NULL,
                      event_uuid UUID NOT NULL,
                      event_type VARCHAR(200) NOT NULL,
                      event_time BIGINT NOT NULL,
                      event_body JSON,
                      PRIMARY KEY (event_uuid)
);

CREATE INDEX customer_index ON customer(domain_id);

CREATE TABLE template (
                        domain_id VARCHAR(50) NOT NULL,
                        event_uuid UUID NOT NULL,
                        event_type VARCHAR(200) NOT NULL,
                        event_time BIGINT NOT NULL,
                        event_body JSON,
                        PRIMARY KEY (event_uuid)
);

CREATE INDEX template_index ON template(domain_id);

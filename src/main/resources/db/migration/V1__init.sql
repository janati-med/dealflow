CREATE TABLE customer (
                          id          BIGSERIAL PRIMARY KEY,
                          name        VARCHAR(255) NOT NULL,
                          company     VARCHAR(255),
                          email       VARCHAR(255) UNIQUE,
                          created_at  TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE deal (
                      id          BIGSERIAL PRIMARY KEY,
                      title       VARCHAR(255) NOT NULL,
                      value       NUMERIC(12,2) NOT NULL DEFAULT 0,
                      stage       VARCHAR(30) NOT NULL DEFAULT 'LEAD',
                      customer_id BIGINT NOT NULL REFERENCES customer(id),
                      owner       VARCHAR(100),
                      created_at  TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE quote (
                       id          BIGSERIAL PRIMARY KEY,
                       deal_id     BIGINT NOT NULL REFERENCES deal(id),
                       status      VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
                       total       NUMERIC(12,2) NOT NULL DEFAULT 0,
                       created_at  TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE quote_item (
                            id          BIGSERIAL PRIMARY KEY,
                            quote_id    BIGINT NOT NULL REFERENCES quote(id),
                            description VARCHAR(255) NOT NULL,
                            quantity    INT NOT NULL,
                            unit_price  NUMERIC(12,2) NOT NULL
);
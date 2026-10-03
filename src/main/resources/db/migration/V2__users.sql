CREATE TABLE app_user (
                          id            BIGSERIAL PRIMARY KEY,
                          username      VARCHAR(100) NOT NULL UNIQUE,
                          password_hash VARCHAR(100) NOT NULL,
                          role          VARCHAR(20)  NOT NULL
);
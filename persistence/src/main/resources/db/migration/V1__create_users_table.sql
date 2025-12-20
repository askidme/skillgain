-- ===============================
-- User sequence
-- ===============================
CREATE SEQUENCE user_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- ===============================
-- Users table
-- ===============================
CREATE TABLE users (
                       id BIGINT NOT NULL DEFAULT nextval('user_seq'),
                       email VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       first_name VARCHAR(255),
                       last_name VARCHAR(255),
                       phone VARCHAR(50),
                       birth_date DATE,
                       profile_picture VARCHAR(512),
                       user_role VARCHAR(50) NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT now(),
                       updated_at TIMESTAMP NOT NULL DEFAULT now(),
                       created_by BIGINT,
                       updated_by BIGINT,
                       CONSTRAINT chk_users_role
                           CHECK (user_role IN ('STUDENT','INSTRUCTOR','ADMIN')),
                       CONSTRAINT pk_users PRIMARY KEY (id),
                       CONSTRAINT uq_users_email UNIQUE (email),
                       CONSTRAINT fk_users_created_by
                           FOREIGN KEY (created_by) REFERENCES users(id),
                       CONSTRAINT fk_users_updated_by
                           FOREIGN KEY (updated_by) REFERENCES users(id)
);

-- ===============================
-- Ownership (optional but recommended)
-- ===============================
ALTER SEQUENCE user_seq OWNED BY users.id;

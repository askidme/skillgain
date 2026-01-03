-- ===============================
-- User sequence
-- ===============================
CREATE SEQUENCE user_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

-- ===============================
-- Users table
-- ===============================
CREATE TABLE users
(
    id                    BIGINT       NOT NULL DEFAULT nextval('user_seq'),
    email                 VARCHAR(255) NOT NULL,
    password              VARCHAR(255), -- nullable for OAuth users
    first_name            VARCHAR(255),
    last_name             VARCHAR(255),
    phone                 VARCHAR(50),
    birth_date            DATE,
    profile_picture       VARCHAR(512),

    -- Account state
    active                BOOLEAN      NOT NULL DEFAULT TRUE,
    email_verified        BOOLEAN      NOT NULL DEFAULT FALSE,
    force_password_change BOOLEAN      NOT NULL DEFAULT FALSE,
    auth_provider         VARCHAR(50)  NOT NULL DEFAULT 'LOCAL',
    provider_user_id      VARCHAR(255), -- Google/GitHub user id

    last_login_at         TIMESTAMP,

    created_at            TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at            TIMESTAMP    NOT NULL DEFAULT now(),
    deleted_at            TIMESTAMP,

    created_by            BIGINT,
    updated_by            BIGINT,
    deleted_by            BIGINT,

    CONSTRAINT pk_users PRIMARY KEY (id),

    CONSTRAINT uq_users_email UNIQUE (email),

    CONSTRAINT fk_users_created_by
        FOREIGN KEY (created_by) REFERENCES users (id),

    CONSTRAINT fk_users_updated_by
        FOREIGN KEY (updated_by) REFERENCES users (id),

    CONSTRAINT fk_users_deleted_by
        FOREIGN KEY (deleted_by) REFERENCES users (id)
);

CREATE INDEX idx_users_deleted_at ON users (deleted_at);
CREATE INDEX idx_users_deleted_by ON users (deleted_by);

CREATE TABLE roles
(
    id          BIGINT PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

INSERT INTO roles (id, name, description)
VALUES (1, 'ROLE_USER', 'Regular learner'),
       (2, 'ROLE_INSTRUCTOR', 'Can teach and assess'),
       (3, 'ROLE_AUTHOR', 'Creates online courses'),
       (4, 'ROLE_ADMIN', 'System administrator');

CREATE TABLE user_roles
(
    user_id     BIGINT    NOT NULL,
    role_id     BIGINT    NOT NULL,

    assigned_at TIMESTAMP NOT NULL DEFAULT now(),
    assigned_by BIGINT,

    CONSTRAINT pk_user_roles PRIMARY KEY (user_id, role_id),

    CONSTRAINT fk_user_roles_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,

    CONSTRAINT fk_user_roles_role
        FOREIGN KEY (role_id) REFERENCES roles (id),

    CONSTRAINT fk_user_roles_assigned_by
        FOREIGN KEY (assigned_by) REFERENCES users (id)
);

CREATE TABLE user_oauth_accounts
(
    id               BIGINT PRIMARY KEY,
    user_id          BIGINT       NOT NULL,

    provider         VARCHAR(50)  NOT NULL,
    provider_user_id VARCHAR(255) NOT NULL,

    access_token     TEXT,
    refresh_token    TEXT,

    created_at       TIMESTAMP    NOT NULL DEFAULT now(),

    CONSTRAINT uq_oauth_provider UNIQUE (provider, provider_user_id),
    CONSTRAINT fk_oauth_user FOREIGN KEY (user_id) REFERENCES users (id)
);


-- ===============================
-- Ownership (optional but recommended)
-- ===============================
ALTER SEQUENCE user_seq OWNED BY users.id;

-- ===============================
-- System admin user
-- ===============================
INSERT INTO users (id,
                   email,
                   password,
                   first_name,
                   last_name,
                   active,
                   email_verified,
                   auth_provider,
                   created_at,
                   updated_at)
VALUES (nextval('user_seq'),
        'admin@skillgain.net',
        '$2a$10$kkjyGsbq/ZQShTnE7WsUdO.RwtXCiNKG.gCNvAigQ9G8bq1E2Chi6',
        'System',
        'Administrator',
        TRUE,
        TRUE,
        'LOCAL',
        now(),
        now());

-- ===============================
-- Assign ROLE_ADMIN to System Administrator
-- ===============================
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
         JOIN roles r ON r.name = 'ROLE_ADMIN'
WHERE u.email = 'admin@skillgain.net';

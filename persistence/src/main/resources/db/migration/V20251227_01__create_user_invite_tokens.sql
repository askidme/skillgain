-- ============================================
-- User invite token sequence
-- ============================================

CREATE SEQUENCE user_invite_token_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

-- ============================================
-- User invite tokens table
-- ============================================

CREATE TABLE user_invite_tokens
(
    id         BIGINT       NOT NULL DEFAULT nextval('user_invite_token_seq'),
    token      VARCHAR(255) NOT NULL,
    user_id    BIGINT       NOT NULL,

    expires_at TIMESTAMP    NOT NULL,
    used       BOOLEAN      NOT NULL DEFAULT FALSE,

    created_at TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at TIMESTAMP    NOT NULL DEFAULT now(),
    deleted_at TIMESTAMP,

    created_by BIGINT,
    updated_by BIGINT,
    deleted_by BIGINT,


    CONSTRAINT pk_user_invite_tokens
        PRIMARY KEY (id),

    CONSTRAINT uq_user_invite_tokens_token
        UNIQUE (token),

    CONSTRAINT fk_user_invite_tokens_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_user_invite_tokens_created_by
        FOREIGN KEY (created_by)
            REFERENCES users (id),

    CONSTRAINT fk_user_invite_tokens_updated_by
        FOREIGN KEY (updated_by)
            REFERENCES users (id),

    CONSTRAINT fk_user_invite_tokens_deleted_by
        FOREIGN KEY (deleted_by) REFERENCES users (id)
);

-- ============================================
-- Indexes
-- ============================================

CREATE INDEX idx_user_invite_tokens_token
    ON user_invite_tokens (token);

CREATE INDEX idx_user_invite_tokens_user_id
    ON user_invite_tokens (user_id);

CREATE INDEX idx_user_invite_tokens_expires_at
    ON user_invite_tokens (expires_at);

CREATE INDEX idx_user_invite_tokens_used
    ON user_invite_tokens (used);

-- ============================================
-- Ownership
-- ============================================

ALTER SEQUENCE user_invite_token_seq
    OWNED BY user_invite_tokens.id;

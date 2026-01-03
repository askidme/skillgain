CREATE SEQUENCE user_password_history_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

CREATE TABLE user_password_history
(
    id            BIGINT       NOT NULL DEFAULT nextval('user_password_history_seq'),
    user_id       BIGINT       NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at    TIMESTAMP    NOT NULL DEFAULT now(),

    CONSTRAINT fk_password_history_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_password_history_user_created
    ON user_password_history (user_id, created_at DESC);

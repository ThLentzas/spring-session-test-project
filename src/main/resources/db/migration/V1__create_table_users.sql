CREATE TYPE role_type AS ENUM (
    'ROLE_CUSTOMER',
    'ROLE_ADMIN'
);

CREATE TABLE IF NOT EXISTS roles (
    id   SERIAL,
    type role_type NOT NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users (
    id       SERIAL,
    username VARCHAR(50) NOT NULL,
    password TEXT        NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users_roles (
    users_id INTEGER NOT NULL,
    roles_id INTEGER NOT NULl,
    CONSTRAINT pk_users_roles PRIMARY KEY (users_id, roles_id),
    CONSTRAINT fk_users_roles_users FOREIGN KEY (users_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_users_roles_roles FOREIGN KEY (roles_id) REFERENCES roles (id) ON DELETE CASCADE
)
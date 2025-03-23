CREATE TABLE articles
(
    id          UUID PRIMARY KEY,
    title       VARCHAR(100) NOT NULL,
    description VARCHAR(250) NOT NULL,
    text        VARCHAR      NOT NULL,
    author      VARCHAR(100) NOT NULL,
    label       VARCHAR(100) NOT NULL,
    image       VARCHAR(500),
    date_create TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_update TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);

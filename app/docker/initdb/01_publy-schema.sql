DROP TABLE IF EXISTS members CASCADE;
DROP TABLE IF EXISTS comments CASCADE;
DROP TABLE IF EXISTS reactions CASCADE;
DROP TABLE IF EXISTS stories CASCADE;

DROP SEQUENCE IF EXISTS comment_ids;
CREATE SEQUENCE comment_ids;

DROP SEQUENCE IF EXISTS reaction_ids;
CREATE SEQUENCE reaction_ids;

DROP SEQUENCE IF EXISTS story_ids;
CREATE SEQUENCE story_ids;


CREATE TABLE members
(
    id                 BIGINT PRIMARY KEY NOT NULL,
    version            INTEGER            NOT NULL,
    created_at         TIMESTAMP          NOT NULL UNIQUE,

    user_id            VARCHAR(10)        NOT NULL UNIQUE,

    profile_image      VARCHAR            NOT NULL,
    location           VARCHAR,
    bio                VARCHAR,
    skills             VARCHAR,
    currently_learning VARCHAR,
    works              VARCHAR
);

CREATE TABLE stories
(
    id            BIGINT PRIMARY KEY             NOT NULL,
    version       INTEGER                        NOT NULL,
    created_at    TIMESTAMP                      NOT NULL,
    written_by_id BIGINT REFERENCES members (id) NOT NULL,
    body_markdown TEXT                           NOT NULL,

    title         VARCHAR                        NOT NULL,
    tags          VARCHAR[]                      NOT NULL
);

CREATE TABLE comments
(
    id            BIGINT PRIMARY KEY             NOT NULL,
    version       INTEGER                        NOT NULL,
    created_at    TIMESTAMP                      NOT NULL,
    written_by_id BIGINT REFERENCES members (id) NOT NULL,
    content       TEXT                           NOT NULL,

    story_id      BIGINT                         NOT NULL REFERENCES stories (id)
);



CREATE TABLE reactions
(
    id          BIGINT PRIMARY KEY             NOT NULL,
    version     INTEGER                        NOT NULL,
    created_at  TIMESTAMP                      NOT NULL,
    given_by_id BIGINT REFERENCES members (id) NOT NULL,

    type        VARCHAR                        NOT NULL,

    story_id    BIGINT                         NOT NULL REFERENCES stories (id),
    UNIQUE (given_by_id, type, story_id)
);


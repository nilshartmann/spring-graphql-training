DELETE FROM REACTIONS;
DELETE FROM COMMENTS;

DELETE FROM STORIES;
DELETE FROM MEMBERS;

INSERT INTO members (id, version, created_at, user_id, profile_image)
VALUES (1, 1, '2019-05-01T21:39:24.849Z', 'U1', 'avatars/u1.jpg');

INSERT INTO members (id, version, created_at, user_id, profile_image)
VALUES (2, 1, '2019-05-02T21:39:24.849Z', 'U2', 'avatars/u1.jpg');

INSERT INTO members (id, version, created_at, user_id, profile_image)
VALUES (3, 1, '2019-05-03T21:39:24.849Z', 'U3', 'avatars/u1.jpg');

INSERT INTO members (id, version, created_at, user_id, profile_image)
VALUES (9, 1, '2019-05-09T21:39:24.849Z', 'U9', 'avatars/u1.jpg');

INSERT INTO members (id, version, created_at, user_id, profile_image)
VALUES (10, 1, '2019-05-10T21:39:24.849Z', 'U10', 'avatars/u1.jpg');

INSERT INTO members (id, version, created_at, user_id, profile_image)
VALUES (11, 1, '2019-05-11T21:39:24.849Z', 'U11', 'avatars/u11.jpg');

INSERT INTO members (id, version, created_at, user_id, profile_image)
VALUES (12, 1, '2019-05-12T21:39:24.849Z', 'U12', 'avatars/u1.jpg');

INSERT INTO members (id, version, created_at, user_id, profile_image)
VALUES (13, 1, '2019-05-13T21:39:24.849Z', 'U13', 'avatars/u1.jpg');

INSERT INTO members (id, version, created_at, user_id, profile_image)
VALUES (15, 1, '2019-05-15T21:39:24.849Z', 'U15', 'avatars/u1.jpg');



INSERT INTO stories (id, version, created_at, written_by_id, title, tags, body_markdown)
VALUES (1, 1, '2021-10-01T20:07:37.917Z', 15,
        'H Story 1', '{"java","graphql"}',
        'Body Story 1');

INSERT INTO stories (id, version, created_at, written_by_id, title, tags, body_markdown, rc)
VALUES (2, 1, '2021-10-02T20:07:37.917Z', 13,
        'D Story 2', '{"graphql","spring"}',
        'Body Story 2', 2);

INSERT INTO stories (id, version, created_at, written_by_id, title, tags, body_markdown, rc)
VALUES (3, 1, '2021-10-03T20:07:37.917Z', 10,
        'A Story 3', '{"spring"}',
        'Body Story 3', 1);

INSERT INTO stories (id, version, created_at, written_by_id, title, tags, body_markdown, rc)
VALUES (4, 1, '2021-10-04T20:07:37.917Z', 15,
        'I Story 4', '{"graphql","java"}',
        'Body Story 4', 3);

INSERT INTO stories (id, version, created_at, written_by_id, title, tags, body_markdown)
VALUES (5, 1, '2021-10-05T20:07:37.917Z', 12,
        'B Story 5', '{"graphl","c#"}',
        'Body Story 5');

-- ===============================================================
--               REACTIONS
-- ===============================================================

-- STORY 2 --
INSERT INTO reactions (id, version, created_at, story_id, given_by_id, type)
VALUES (1, 2, '2021-10-05T01:04:07.063Z', 2,
        1, 'laugh');

INSERT INTO reactions (id, version, created_at, story_id, given_by_id, type)
VALUES (2, 1, '2021-10-06T01:27:14.051Z', 2,
        2, 'prosit');

-- STORY 3 --
INSERT INTO reactions (id, version, created_at, story_id, given_by_id, type)
VALUES (3, 1, '2021-10-07T01:05:07.063Z', 3,
        2, 'laugh');

-- STORY 4 --
INSERT INTO reactions (id, version, created_at, story_id, given_by_id, type)
VALUES (4, 1, '2021-10-08T01:06:07.063Z', 4,
        1, 'laugh');
INSERT INTO reactions (id, version, created_at, story_id, given_by_id, type)
VALUES (5, 1, '2021-10-09T01:07:07.063Z', 4,
        1, 'prosit');
INSERT INTO reactions (id, version, created_at, story_id, given_by_id, type)
VALUES (6, 1, '2021-10-10T01:08:07.063Z', 4,
        2, 'laugh');

-- ===============================================================
--               COMMENTS
-- ===============================================================

-- STORY 1 --
INSERT INTO comments (id, version, created_at, story_id, written_by_id, content)
VALUES (1, 1, '2021-10-04T20:38:26.656Z', 1, 1, 'Comment 1');

INSERT INTO comments (id, version, created_at, story_id, written_by_id, content)
VALUES (2, 1, '2021-10-05T20:38:26.656Z', 1, 11, 'Comment 2');

INSERT INTO comments (id, version, created_at, story_id, written_by_id, content)
VALUES (4, 1, '2021-10-07T20:39:26.656Z', 1, 9, 'Comment 4');

INSERT INTO comments (id, version, created_at, story_id, written_by_id, content)
VALUES (5, 1, '2021-10-08T20:40:26.656Z', 1, 3, 'Comment 5');

INSERT INTO comments (id, version, created_at, story_id, written_by_id, content)
VALUES (7, 1, '2021-10-10T20:38:26.656Z', 1, 1, 'Comment 7');

-- STORY 3 --
INSERT INTO comments (id, version, created_at, story_id, written_by_id, content)
VALUES (3, 1, '2021-10-06T20:38:26.656Z', 3, 1, 'Comment 3');

-- STORY 5 --
INSERT INTO comments (id, version, created_at, story_id, written_by_id, content)
VALUES (6, 1, '2021-10-09T20:38:26.656Z', 5, 3, 'Comment 6');

INSERT INTO comments (id, version, created_at, story_id, written_by_id, content)
VALUES (8, 1, '2021-10-11T20:42:26.656Z', 5, 1, 'Comment 8');

SELECT setval('comment_ids', 100);
SELECT setval('reaction_ids', 100);
SELECT setval('story_ids', 100);
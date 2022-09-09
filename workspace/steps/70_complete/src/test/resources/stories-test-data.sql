DELETE FROM REACTIONS;
DELETE FROM COMMENTS;

DELETE FROM STORIES;
DELETE FROM MEMBERS;

INSERT INTO members (id, version, created_at, user_id, profile_image)
VALUES (1, 1, '2019-05-01T21:39:24.849Z', 'U1', 'avatars/u1.jpg');

INSERT INTO members (id, version, created_at, user_id, profile_image)
VALUES (15, 1, '2019-05-15T21:39:24.849Z', 'U15', 'avatars/u1.jpg');


INSERT INTO stories (id, version, created_at, written_by_id, title, tags, body_markdown)
VALUES (1, 1, '2021-10-01T20:07:37.917Z', 15,
        'H Story 1', '{"java","graphql"}',
        'Body Story 1');


SELECT setval('comment_ids', 100);
SELECT setval('reaction_ids', 100);
SELECT setval('story_ids', 100);
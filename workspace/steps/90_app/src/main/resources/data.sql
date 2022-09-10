INSERT INTO members (id, version, created_at, user_id, profile_image, location, bio, skills, currently_learning, works)
VALUES (1, 1, '2019-05-01T21:39:24.849Z', 'U1', 'avatars/nils.jpg', 'Hamburg', 'Software-Developer from Hamburg',
        'Beer and GraphQL', 'How to teach GraphQL', 'Freelancer');
INSERT INTO members (id, version, created_at, user_id, profile_image, location, bio, skills, currently_learning, works)
VALUES (2, 1, '2021-10-08T20:32:15.761Z', 'U2', 'avatars/avatar_U2.png',
        'Longmont, VN', 'quaerat-nihil-eveniet', 'WordPress, Visual Basic, Linux, C#', null, null);
INSERT INTO members (id, version, created_at, user_id, profile_image, location, bio, skills, currently_learning, works)
VALUES (3, 1, '2020-05-06T03:46:48.899Z', 'U3', 'avatars/avatar_U3.png',
        'Coral Springs, BR', null, 'Drupal, WordPress, DevOps', null, 'Weissnat Group');
INSERT INTO members (id, version, created_at, user_id, profile_image, location, bio, skills, currently_learning, works)
VALUES (4, 1, '2021-04-27T01:00:24.481Z', 'U4', 'avatars/avatar_U4.png',
        'Seattle, CI', null, 'Spring', 'Omnis eos mollitia aperiam eos voluptas at consequatur.', null);
INSERT INTO members (id, version, created_at, user_id, profile_image, location, bio, skills, currently_learning, works)
VALUES (5, 1, '2020-08-28T14:58:47.401Z', 'U5', 'avatars/avatar_U5.png',
        'Carrollton, UM', 'provident-sunt-minima', 'GraphQL',
        'Quisquam dolore exercitationem laboriosam aut maiores aut sint.', 'Bode Group');

INSERT INTO stories (id, version, created_at, written_by_id, title, tags, body_markdown)
VALUES (1, 1, '2021-10-09T04:40:50.027Z', 1,
        'Story 1', '{"java","string"}',
        'At vero eos et accusam et justo duo dolores et ea rebum. ');

INSERT INTO stories (id, version, created_at, written_by_id, title, tags, body_markdown)
VALUES (2, 1, '2021-10-11T04:40:50.027Z', 1,
        'Story 2', '{"java","string"}',
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr');

INSERT INTO stories (id, version, created_at, written_by_id, title, tags, body_markdown)
VALUES (3, 1, '2021-10-11T07:52:50.027Z', 2,
        'Story 3', '{"java","string"}',
        'Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui ');

INSERT INTO stories (id, version, created_at, written_by_id, title, tags, body_markdown)
VALUES (4, 1, '2021-10-12T12:34:50.027Z', 3,
        'Story 4', '{"java","string"}',
        'Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. ');

INSERT INTO stories (id, version, created_at, written_by_id, title, tags, body_markdown)
VALUES (5, 1, '2021-10-13T17:12:50.027Z', 2,
        'Story 5', '{"java","string"}',
        'Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. ');

INSERT INTO stories (id, version, created_at, written_by_id, title, tags, body_markdown)
VALUES (6, 1, '2021-10-13T17:44:50.027Z', 1,
        'Story 6', '{"java","string"}',
        'Nam liber tempor cum soluta nobis eleifend option congue nihil imperdiet doming id quod mazim placerat facer possim assum. L');
INSERT INTO stories (id, version, created_at, written_by_id, title, tags, body_markdown)
VALUES (7, 1, '2021-10-13T18:31:22.027Z', 4,
        'Story 7', '{"java","string"}',
        'Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis.');
INSERT INTO stories (id, version, created_at, written_by_id, title, tags, body_markdown)
VALUES (8, 1, '2021-10-14T12:34:50.027Z', 3,
        'Story 8', '{"java","string"}',
        'At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. ');
INSERT INTO stories (id, version, created_at, written_by_id, title, tags, body_markdown)
VALUES (9, 1, '2021-10-14T13:40:50.027Z', 3,
        'Story 9', '{"java","string"}',
        'Consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. ');
INSERT INTO stories (id, version, created_at, written_by_id, title, tags, body_markdown)
VALUES (10, 1, '2021-10-15T13:32:50.027Z', 2,
        'Story 10', '{"java","string"}',
        'Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at ');


INSERT INTO reactions (id, version, created_at, story_id, given_by_id, type)
VALUES (1, 1, '2021-10-09T13:52:10.259Z', 10,
        2, 'heart');

INSERT INTO comments (id, version, created_at, story_id, written_by_id, content)
VALUES (2, 1, '2021-10-10T05:53:21.912Z', 10, 4,
        'Maiores autem adipisci.');

ALTER SEQUENCE comment_ids RESTART WITH 171;
ALTER SEQUENCE reaction_ids RESTART WITH 610;
ALTER SEQUENCE story_ids RESTART WITH 101;
-- SELECT setval('comment_ids', 171);
-- SELECT setval('reaction_ids', 610);
-- SELECT setval('story_ids', 101);
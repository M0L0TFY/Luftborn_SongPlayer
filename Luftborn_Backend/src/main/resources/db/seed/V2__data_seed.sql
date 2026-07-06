-- =========================================================
-- USERS (10) - all act as artists for the seeded albums/songs.
-- =========================================================
INSERT INTO users (id, username, email, hashed_password, created_at) VALUES
                                                                         (1,  'alice_music',    'alice@example.com',   'PLACEHOLDER_HASH', '2024-01-10T09:00:00Z'),
                                                                         (2,  'bob_beats',      'bob@example.com',     'PLACEHOLDER_HASH', '2024-01-12T09:00:00Z'),
                                                                         (3,  'charlie_tunes',  'charlie@example.com', 'PLACEHOLDER_HASH', '2024-01-15T09:00:00Z'),
                                                                         (4,  'diana_sound',    'diana@example.com',   'PLACEHOLDER_HASH', '2024-02-01T09:00:00Z'),
                                                                         (5,  'ethan_waves',    'ethan@example.com',   'PLACEHOLDER_HASH', '2024-02-10T09:00:00Z'),
                                                                         (6,  'fiona_notes',    'fiona@example.com',   'PLACEHOLDER_HASH', '2024-02-20T09:00:00Z'),
                                                                         (7,  'george_rhythm',  'george@example.com',  'PLACEHOLDER_HASH', '2024-03-01T09:00:00Z'),
                                                                         (8,  'hannah_melody',  'hannah@example.com',  'PLACEHOLDER_HASH', '2024-03-15T09:00:00Z'),
                                                                         (9,  'ian_harmony',    'ian@example.com',     'PLACEHOLDER_HASH', '2024-04-01T09:00:00Z'),
                                                                         (10, 'julia_chords',   'julia@example.com',   'PLACEHOLDER_HASH', '2024-04-10T09:00:00Z');

SELECT setval(pg_get_serial_sequence('users', 'id'), (SELECT MAX(id) FROM users));

-- =========================================================
-- ALBUMS (10) - one per artist, artist_id maps 1:1 to users.id above.
-- =========================================================
INSERT INTO albums (id, title, artist_id, release_date, uploaded_at) VALUES
                                                                         (1,  'Echoes of Dawn',   1,  '2024-03-01', '2024-03-01T10:00:00Z'),
                                                                         (2,  'Midnight Drive',   2,  '2024-03-15', '2024-03-15T10:00:00Z'),
                                                                         (3,  'Silent Waves',     3,  '2024-04-01', '2024-04-01T10:00:00Z'),
                                                                         (4,  'Golden Hour',      4,  '2024-04-20', '2024-04-20T10:00:00Z'),
                                                                         (5,  'Neon Dreams',      5,  '2024-05-05', '2024-05-05T10:00:00Z'),
                                                                         (6,  'Broken Glass',     6,  '2024-05-20', '2024-05-20T10:00:00Z'),
                                                                         (7,  'Coastal Skies',    7,  '2024-06-01', '2024-06-01T10:00:00Z'),
                                                                         (8,  'Wildfire',         8,  '2024-06-15', '2024-06-15T10:00:00Z'),
                                                                         (9,  'Paper Moon',       9,  '2024-07-01', '2024-07-01T10:00:00Z'),
                                                                         (10, 'Velvet Streets',   10, '2024-07-15', '2024-07-15T10:00:00Z');

SELECT setval(pg_get_serial_sequence('albums', 'id'), (SELECT MAX(id) FROM albums));

-- =========================================================
-- SONGS (30) - 24 belong to albums 1-8 (3 tracks each), matching that
-- =========================================================
INSERT INTO songs (id, title, artist_id, album_id, duration_seconds, release_date, uploaded_at) VALUES
-- Album 1 - Echoes of Dawn (artist 1)
(1,  'Echoes of Dawn - Intro',     1, 1, 145, '2024-03-01', '2024-03-01T10:05:00Z'),
(2,  'Rising Light',               1, 1, 210, '2024-03-01', '2024-03-01T10:06:00Z'),
(3,  'Fading Stars',               1, 1, 198, '2024-03-01', '2024-03-01T10:07:00Z'),

-- Album 2 - Midnight Drive (artist 2)
(4,  'Midnight Drive',             2, 2, 220, '2024-03-15', '2024-03-15T10:05:00Z'),
(5,  'City Lights',                2, 2, 205, '2024-03-15', '2024-03-15T10:06:00Z'),
(6,  'Empty Roads',                2, 2, 189, '2024-03-15', '2024-03-15T10:07:00Z'),

-- Album 3 - Silent Waves (artist 3)
(7,  'Silent Waves',               3, 3, 240, '2024-04-01', '2024-04-01T10:05:00Z'),
(8,  'Undertow',                   3, 3, 175, '2024-04-01', '2024-04-01T10:06:00Z'),
(9,  'Shoreline',                  3, 3, 230, '2024-04-01', '2024-04-01T10:07:00Z'),

-- Album 4 - Golden Hour (artist 4)
(10, 'Golden Hour',                4, 4, 200, '2024-04-20', '2024-04-20T10:05:00Z'),
(11, 'Sunset Boulevard',           4, 4, 215, '2024-04-20', '2024-04-20T10:06:00Z'),
(12, 'Amber Skies',                4, 4, 190, '2024-04-20', '2024-04-20T10:07:00Z'),

-- Album 5 - Neon Dreams (artist 5)
(13, 'Neon Dreams',                5, 5, 250, '2024-05-05', '2024-05-05T10:05:00Z'),
(14, 'Electric Nights',            5, 5, 195, '2024-05-05', '2024-05-05T10:06:00Z'),
(15, 'Digital Horizon',            5, 5, 208, '2024-05-05', '2024-05-05T10:07:00Z'),

-- Album 6 - Broken Glass (artist 6)
(16, 'Broken Glass',               6, 6, 180, '2024-05-20', '2024-05-20T10:05:00Z'),
(17, 'Shattered',                  6, 6, 165, '2024-05-20', '2024-05-20T10:06:00Z'),
(18, 'Mend',                       6, 6, 222, '2024-05-20', '2024-05-20T10:07:00Z'),

-- Album 7 - Coastal Skies (artist 7)
(19, 'Coastal Skies',              7, 7, 214, '2024-06-01', '2024-06-01T10:05:00Z'),
(20, 'Salt Air',                   7, 7, 199, '2024-06-01', '2024-06-01T10:06:00Z'),
(21, 'Tide Pool',                  7, 7, 187, '2024-06-01', '2024-06-01T10:07:00Z'),

-- Album 8 - Wildfire (artist 8)
(22, 'Wildfire',                   8, 8, 233, '2024-06-15', '2024-06-15T10:05:00Z'),
(23, 'Ashes to Ember',             8, 8, 201, '2024-06-15', '2024-06-15T10:06:00Z'),
(24, 'Firelight',                  8, 8, 178, '2024-06-15', '2024-06-15T10:07:00Z'),

-- Singles (album_id NULL)
(25, 'Standalone Heart',           9,  NULL, 205, '2024-07-10', '2024-07-10T09:00:00Z'),
(26, 'One More Night',             10, NULL, 192, '2024-07-20', '2024-07-20T09:00:00Z'),
(27, 'Quiet Storm',                1,  NULL, 168, '2024-08-01', '2024-08-01T09:00:00Z'),
(28, 'Afterglow',                  3,  NULL, 210, '2024-08-10', '2024-08-10T09:00:00Z'),
(29, 'Runaway',                    5,  NULL, 199, '2024-08-20', '2024-08-20T09:00:00Z'),
(30, 'Last Call',                  7,  NULL, 221, '2024-09-01', '2024-09-01T09:00:00Z');

SELECT setval(pg_get_serial_sequence('songs', 'id'), (SELECT MAX(id) FROM songs));
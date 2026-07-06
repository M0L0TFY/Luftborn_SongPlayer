CREATE TABLE users (
                       id               BIGSERIAL PRIMARY KEY,
                       username         VARCHAR(50)  NOT NULL UNIQUE,
                       email            VARCHAR(255) NOT NULL UNIQUE,
                       hashed_password  VARCHAR(255),
                       created_at       TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE TABLE albums (
                        id            BIGSERIAL PRIMARY KEY,
                        title         VARCHAR(255) NOT NULL,
                        artist_id     BIGINT       NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                        release_date  DATE         NOT NULL,
                        uploaded_at   TIMESTAMP    NOT NULL DEFAULT now()
);
CREATE INDEX idx_album_artist ON albums(artist_id);

CREATE TABLE songs (
                       id                BIGSERIAL PRIMARY KEY,
                       title             VARCHAR(255) NOT NULL,
                       artist_id         BIGINT       NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                       album_id          BIGINT       REFERENCES albums(id) ON DELETE SET NULL,
                       duration_seconds  INTEGER      NOT NULL CHECK (duration_seconds > 0),
                       release_date      DATE         NOT NULL,
                       uploaded_at       TIMESTAMP    NOT NULL DEFAULT now()
);
CREATE INDEX idx_song_artist ON songs(artist_id);
CREATE INDEX idx_song_album  ON songs(album_id);

CREATE TABLE playlists (
                           id                      BIGSERIAL PRIMARY KEY,
                           name                    VARCHAR(255) NOT NULL,
                           description             VARCHAR(1000),
                           total_duration_seconds  INTEGER NOT NULL DEFAULT 0,
                           owner_id                BIGINT  NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                           is_public               BOOLEAN NOT NULL DEFAULT false
);
CREATE INDEX idx_playlist_owner ON playlists(owner_id);

CREATE TABLE playlist_songs (
                                id           BIGSERIAL PRIMARY KEY,
                                playlist_id  BIGINT NOT NULL REFERENCES playlists(id) ON DELETE CASCADE,
                                song_id      BIGINT NOT NULL REFERENCES songs(id) ON DELETE CASCADE,
                                added_at     TIMESTAMP NOT NULL DEFAULT now()
);
CREATE INDEX idx_song_playlist ON playlist_songs(song_id, playlist_id);
CREATE INDEX idx_playlist      ON playlist_songs(playlist_id);
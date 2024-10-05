CREATE TABLE IF NOT EXISTS rating_mpa (
    id TINYINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    rating VARCHAR(5) NOT NULL
);

CREATE TABLE IF NOT EXISTS genre (
    id TINYINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(40) NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(200) NOT NULL,
    release_date DATE NOT NULL,
    duration SMALLINT NOT NULL,
    rating_id TINYINT NOT NULL REFERENCES rating_mpa(id)
);

CREATE TABLE IF NOT EXISTS genres (
    film_id BIGINT NOT NULL REFERENCES films(id),
    genre_id TINYINT NOT NULL REFERENCES genre(id),
    CONSTRAINT uq_genres UNIQUE(film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS friend_request (
    id TINYINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    status BIT NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255),
    login VARCHAR(40) NOT NULL,
    email VARCHAR(255) NOT NULL,
    birthday DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS friends (
    user_id BIGINT NOT NULL REFERENCES users(id),
    friend_id BIGINT NOT NULL REFERENCES users(id),
    friend_request_id TINYINT NOT NULL REFERENCES friend_request(id)
);

CREATE TABLE IF NOT EXISTS likes (
    film_id BIGINT NOT NULL REFERENCES films(id),
    user_id BIGINT NOT NULL REFERENCES users(id)
);
SET REFERENTIAL_INTEGRITY FALSE;
TRUNCATE TABLE rating_mpa RESTART IDENTITY;
TRUNCATE TABLE genre RESTART IDENTITY;
TRUNCATE TABLE films RESTART IDENTITY;
TRUNCATE TABLE genres RESTART IDENTITY;
TRUNCATE TABLE users RESTART IDENTITY;
TRUNCATE TABLE friends RESTART IDENTITY;
TRUNCATE TABLE likes RESTART IDENTITY;
TRUNCATE TABLE reviews RESTART IDENTITY;
TRUNCATE TABLE reviews_likes RESTART IDENTITY;
TRUNCATE TABLE reviews_dislikes RESTART IDENTITY;
SET REFERENTIAL_INTEGRITY TRUE;

INSERT INTO rating_mpa (rating)
VALUES ('G'),
       ('PG'),
       ('PG-13'),
       ('R'),
       ('NC-17');

INSERT INTO genre (name)
VALUES ('Комедия'),
       ('Драма'),
       ('Мультфильм'),
       ('Триллер'),
       ('Документальный'),
       ('Боевик');

INSERT INTO films (name, description, release_date, duration, rating_id)
VALUES ('The Shawshank Redemption', 'A banker convicted...', '1994-10-14', 142, 4),
       ('The Godfather', 'The aging patriarch...', '1972-03-24', 175, 4),
       ('The Dark Knight', 'When a menace known as the Joker...', '2008-07-18', 152, 3),
       ('It''s a Wonderful Life', 'An angel is sent from Heaven...', '1947-01-07', 130, 2),
       ('The Lion King', 'Lion prince Simba and his father...', '1994-06-24', 88, 1);

INSERT INTO genres (film_id, genre_id)
VALUES (1, 2),
       (2, 3), (2, 2),
       (3, 6), (3, 5), (3, 2),
       (4, 2), (4, 6), (4, 3),
       (5, 3), (5, 5), (5, 2);

INSERT INTO users (name, login, email, birthday)
VALUES ('', 'userlogin', 'mail@mail.com', '1995-05-05'),
       ('', 'testlogin', 'test@mail.ru', '2000-01-01'),
       ('name', 'unknown', 'name@unknown.com', '1950-12-12'),
       ('no name', 'someuser', 'some@mail.com', '1999-10-01'),
       ('simple name', 'simple_login', 'simple@mail.ru', '2010-10-10');

INSERT INTO friends (user_id, friend_id)
VALUES (1, 2), (1, 3), (1, 4), (1, 5),
       (2, 1), (2, 3), (2, 4), (2, 5),
       (3, 2), (3, 4), (3, 5),
       (4, 1), (4, 2), (4, 3), (4, 5),
       (5, 2), (5, 4);

INSERT INTO likes (film_id, user_id)
VALUES (1, 1), (1, 2), (1, 3), (1, 4), (1, 5),
       (2, 1), (2, 3),
       (3, 1), (3, 3), (3, 5),
       (4, 3),
       (5, 2), (5, 3), (5, 4), (5, 5);

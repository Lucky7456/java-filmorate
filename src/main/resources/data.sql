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

INSERT INTO friend_request (status)
VALUES (0),
       (1);

INSERT INTO users (name, login, email, birthday)
VALUES ('', 'userlogin', 'mail@mail.com', '1995-05-05'),
       ('', 'testlogin', 'test@mail.ru', '2000-01-01'),
       ('name', 'unknown', 'name@unknown.com', '1950-12-12'),
       ('no name', 'someuser', 'some@mail.com', '1999-10-01'),
       ('simple name', 'simple_login', 'simple@mail.ru', '2010-10-10');

INSERT INTO friends (user_id, friend_id, friend_request_id)
VALUES (1, 2, 2), (1, 3, 1), (1, 4, 2), (1, 5, 1),
       (2, 3, 2), (2, 4, 2), (2, 5, 2),
       (3, 4, 2), (3, 5, 1),
       (4, 5, 2);

INSERT INTO likes (film_id, user_id)
VALUES (1, 1), (1, 2), (1, 3), (1, 4), (1, 5),
       (2, 1), (2, 3),
       (3, 1), (3, 3), (3, 5),
       (4, 3),
       (5, 2), (5, 3), (5, 4), (5, 5);
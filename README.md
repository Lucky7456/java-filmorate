# java-filmorate
## Схема базы данных
![database schema](dbschema.png)


### Примеры запросов

- для получения списка всех фильмов:
  ```sql
  SELECT *
  FROM film AS f
  JOIN rating_mpa AS r 
    ON r.id = f.rating_id;
  ```
- для получения списка всех пользователей:
  ```sql
  SELECT *
  FROM user;
  ```
- список 10 наиболее популярных фильмов:
  ```sql
  SELECT f.name,
         f.description,
         f.release_date,
         f.duration,
         r.rating,
         COUNT(l.user_id) AS popularity
  FROM films AS f
  JOIN rating_mpa AS r ON r.id = f.rating_id
  JOIN likes AS l ON l.film_id = f.id
  GROUP BY l.film_id
  ORDER BY popularity DESC
  LIMIT 10;
  ```
- список общих друзей `@user_1` и `@user_2`
  ```sql
  SELECT *
  FROM users
  WHERE id IN (SELECT f1.friend_id
               FROM friends AS f1
               JOIN friends AS f2 ON f1.friend_id = f2.friend_id
               JOIN friend_request AS fr ON fr.id = f1.friend_request_id AND fr.id = f2.friend_request_id
               WHERE fr.status AND f1.user_id = @user_1 AND f2.user_id = @user_2
  );
  ```
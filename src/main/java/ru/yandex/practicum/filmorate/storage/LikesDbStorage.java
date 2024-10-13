package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.interfaces.LikesStorage;
import ru.yandex.practicum.filmorate.storage.util.BaseInsertDeleteStorage;

@Repository
public class LikesDbStorage extends BaseInsertDeleteStorage implements LikesStorage {
    private static final String LIKE_INSERT_QUERY =
            "INSERT INTO likes (film_id, user_id) " +
            "VALUES (?, ?)";
    private static final String LIKE_DELETE_QUERY =
            "DELETE FROM likes " +
            "WHERE film_id = ? AND user_id = ?";

    public LikesDbStorage(JdbcTemplate jdbc) {
        super(jdbc, LIKE_INSERT_QUERY, LIKE_DELETE_QUERY);
    }
}
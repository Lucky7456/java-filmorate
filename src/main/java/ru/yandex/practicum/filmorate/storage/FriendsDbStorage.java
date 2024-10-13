package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.interfaces.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.util.BaseInsertDeleteStorage;

@Repository
public class FriendsDbStorage extends BaseInsertDeleteStorage implements FriendsStorage {
    private static final String FRIENDS_INSERT_QUERY =
            "INSERT INTO friends (user_id, friend_id) " +
            "VALUES (?, ?)";
    private static final String FRIENDS_DELETE_QUERY =
            "DELETE FROM friends " +
            "WHERE user_id = ? AND friend_id = ?";

    public FriendsDbStorage(JdbcTemplate jdbc) {
        super(jdbc, FRIENDS_INSERT_QUERY, FRIENDS_DELETE_QUERY);
    }
}
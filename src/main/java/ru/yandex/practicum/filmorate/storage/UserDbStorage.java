package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.storage.util.BaseCrudStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDbStorage extends BaseCrudStorage<User> implements UserStorage {
    private static final String TABLE_NAME = "users";
    private static final String FIND_ALL_FRIENDS_QUERY =
            "SELECT * FROM users " +
            "WHERE id IN (SELECT friend_id " +
            "FROM friends " +
            "WHERE user_id = ?)";
    private static final String UPDATE_QUERY =
            "UPDATE users SET name = ?, login = ?, email = ?, birthday = ? " +
            "WHERE id = ?";
    private static final String FIND_ALL_MUTUAL_FRIENDS_QUERY =
            "SELECT * FROM users " +
            "WHERE id IN (SELECT f1.friend_id " +
            "FROM friends AS f1 " +
            "JOIN friends AS f2 ON f1.friend_id = f2.friend_id " +
            "WHERE f1.user_id = ? AND f2.user_id = ?)";

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper, TABLE_NAME, FIND_ALL_FRIENDS_QUERY, UPDATE_QUERY);
    }

    @Override
    public List<User> findAllMutualFriends(long userId, long otherId) {
        return findMany(FIND_ALL_MUTUAL_FRIENDS_QUERY, userId, otherId);
    }

    @Override
    protected Map<String, Object> toMap(User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", user.getName());
        values.put("login", user.getLogin());
        values.put("email", user.getEmail());
        values.put("birthday", user.getBirthday());
        return values;
    }
}
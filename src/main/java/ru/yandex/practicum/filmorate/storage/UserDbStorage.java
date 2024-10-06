package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UserDbStorage extends BaseCrudStorage<User> implements UserStorage {
    private static final String TABLE_NAME = "users";
    private static final String FIND_ALL_QUERY =
            "SELECT * FROM users";
    private static final String FIND_ALL_FRIENDS_QUERY =
            "SELECT * FROM users " +
            "WHERE id in (SELECT f.friend_id " +
            "FROM friends AS f " +
            "JOIN friend_request AS fr ON f.friend_request_id = fr.id " +
            "WHERE f.user_id = ?)";
    private static final String FIND_ALL_MUTUAL_FRIENDS_QUERY =
            "SELECT * " +
            "FROM users " +
            "WHERE id IN (SELECT f1.friend_id " +
            "FROM friends AS f1 " +
            "JOIN friends AS f2 ON f1.friend_id = f2.friend_id " +
            "JOIN friend_request AS fr ON fr.id = f1.friend_request_id " +
            "WHERE f1.user_id = ? AND f2.user_id = ?)";
    private static final String UPDATE_QUERY =
            "UPDATE users SET name = ?, login = ?, email = ?, birthday = ? " +
            "WHERE id = ?";
    private static final String DELETE_QUERY =
            "DELETE FROM users WHERE id = ?";
    private static final String FRIEND_REQUEST_EXISTS_QUERY =
            "SELECT 1 " +
            "FROM friends AS f " +
            "JOIN friend_request AS fr ON f.friend_request_id = fr.id " +
            "WHERE f.friend_id = ? AND f.user_id = ? AND NOT fr.status";
    private static final String FRIENDS_UPDATE_QUERY =
            "UPDATE friends " +
            "SET friend_request_id = (SELECT id " +
            "FROM friend_request " +
            "WHERE status) " +
            "WHERE friend_id = ? AND user_id = ?";
    private static final String USERS_EXISTS_QUERY =
            "SELECT COUNT(*) " +
            "FROM users " +
            "WHERE id IN (?, ?)";
    private static final String FRIENDS_INSERT_QUERY =
            "INSERT INTO friends (user_id, friend_id, friend_request_id) " +
            "VALUES (?, ?, (SELECT id FROM friend_request WHERE NOT status LIMIT 1))";
    private static final String FRIENDS_DELETE_QUERY =
            "DELETE FROM friends " +
            "WHERE user_id = ? AND friend_id = ?";
    private static final String FIND_BY_ID_QUERY =
            "SELECT * FROM users WHERE id = ?";
    
    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper, FIND_ALL_QUERY, FIND_BY_ID_QUERY, FIND_ALL_FRIENDS_QUERY, TABLE_NAME, UPDATE_QUERY, DELETE_QUERY);
    }
    
    @Override
    public Collection<User> findAllMutualFriends(long userId, long otherId) {
        if (count(USERS_EXISTS_QUERY, userId, otherId).orElse(0) == 2) {
            return findMany(FIND_ALL_MUTUAL_FRIENDS_QUERY, userId, otherId);
        }
        throw new NotFoundException("users not found");
    }
    
    @Override
    public int addFriend(long userId, long friendId) {
        if (count(FRIEND_REQUEST_EXISTS_QUERY, userId, friendId).orElse(0) == 1) {
            return update(FRIENDS_UPDATE_QUERY, userId, friendId);
        } else if (count(USERS_EXISTS_QUERY, userId, friendId).orElse(0) == 2) {
            return update(FRIENDS_INSERT_QUERY, userId, friendId);
        }
        throw new NotFoundException("users not found");
    }
    
    @Override
    public int removeFriend(long userId, long friendId) {
        if (count(USERS_EXISTS_QUERY, userId, friendId).orElse(0) == 2) {
            return update(FRIENDS_DELETE_QUERY, userId, friendId);
        }
        throw new NotFoundException("user not found");
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

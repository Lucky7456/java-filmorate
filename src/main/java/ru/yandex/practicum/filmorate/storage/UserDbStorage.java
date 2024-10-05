package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.BaseStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.*;

@Repository
public class UserDbStorage extends BaseStorage<User> implements UserStorage {
    private static final String TABLE_NAME = "users";
    private static final String FIND_ALL_QUERY =
            "SELECT * FROM users";
    private static final String FIND_ALL_FRIENDS_QUERY =
            "SELECT * FROM users " +
            "WHERE id in (SELECT user_id " +
            "FROM friends AS f " +
            "JOIN friend_request AS fr ON f.friend_request_id = fr.id " +
            "WHERE friend_id = ?)";
    private static final String FIND_ALL_MUTUAL_FRIENDS_QUERY =
            "SELECT * " +
            "FROM users " +
            "WHERE id IN (SELECT f1.user_id " +
            "FROM friends AS f1 " +
            "JOIN friends AS f2 ON f1.user_id = f2.user_id" +
            "JOIN friend_request AS fr ON fr.id = f1.friend_request_id AND fr.id = f2.friend_request_id " +
            "WHERE f1.friend_id = 2 AND f2.friend_id = 4)";
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
            "UPDATE friends AS f " +
            "SET f.friend_request_id = (SELECT id " +
            "FROM friend_request " +
            "WHERE status) " +
            "WHERE friend_id = ? AND user_id = ?";
    private static final String USERS_EXISTS_QUERY =
            "SELECT COUNT(*) " +
            "FROM users " +
            "WHERE id IN (?, ?)";
    private static final String FRIENDS_INSERT_QUERY =
            "INSERT INTO friends (user_id, friend_id, friend_request_id) " +
            "VALUES (?, ?, (SELECT id FROM friend_request WHERE NOT status))";
    private static final String FRIENDS_DELETE_QUERY =
            "DELETE FROM friends " +
            "WHERE ? IN (user_id, friend_id) AND ? IN (user_id, friend_id)";
    private static final String FIND_BY_ID_QUERY =
            "SELECT * FROM users WHERE id = ?";
    
    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }
    
    @Override
    public Collection<User> findAll() {
        return findMany(FIND_ALL_QUERY);
    }
    
    @Override
    public Collection<User> findAllFriends(long userId) {
        return findMany(FIND_ALL_FRIENDS_QUERY, userId);
    }
    
    @Override
    public Collection<User> findAllMutualFriends(long userId, long otherId) {
        return findMany(FIND_ALL_MUTUAL_FRIENDS_QUERY, userId, otherId);
    }
    
    @Override
    public User create(User user) {
        user.setId(simpleInsert(toMap(user), TABLE_NAME));
        return user;
    }
    
    @Override
    public User update(User user) {
        update(
                UPDATE_QUERY,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }
    
    @Override
    public boolean delete(User user) {
        return update(DELETE_QUERY, user.getId());
    }
    
    @Override
    public boolean addFriend(long userId, long friendId) {
        if (exists(FRIEND_REQUEST_EXISTS_QUERY, userId, friendId) == 1) {
            return update(FRIENDS_UPDATE_QUERY, userId, friendId);
        } else if (exists(USERS_EXISTS_QUERY, userId, friendId) == 2) {
            return update(FRIENDS_INSERT_QUERY, userId, friendId);
        }
        throw new NotFoundException("users not found");
    }
    
    @Override
    public boolean removeFriend(long userId, long friendId) {
        return update(FRIENDS_DELETE_QUERY, userId, friendId);
    }
    
    @Override
    public Optional<User> getUserById(long userId) {
        return findOne(FIND_BY_ID_QUERY, userId);
    }
    
    private Map<String, Object> toMap(User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", user.getName());
        values.put("login", user.getLogin());
        values.put("email", user.getEmail());
        values.put("birthday", user.getBirthday());
        return values;
    }
}

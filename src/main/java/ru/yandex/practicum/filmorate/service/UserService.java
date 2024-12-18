package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        log.debug("create {}", user);
        user.setId(userStorage.create(user));
        return user;
    }

    public User update(User user) {
        log.debug("update {}", user);
        int updatedRows = userStorage.update(
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday(),
                user.getId()
        );
        if (updatedRows == 1) {
            return user;
        }
        throw new NoSuchElementException("user not found");
    }

    public void addFriend(long userId, long friendId) {
        log.debug("userId {} addFriend {}", userId, friendId);
        friendsStorage.insert(getUserById(userId).getId(), getUserById(friendId).getId());
    }

    public void removeFriend(long userId, long friendId) {
        log.debug("userId {} removeFriend {}", userId, friendId);
        friendsStorage.delete(getUserById(userId).getId(), getUserById(friendId).getId());
    }

    public List<User> findFriends(long userId) {
        log.debug("findFriends {}", userId);
        return userStorage.findAllBy(getUserById(userId).getId());
    }

    public List<User> getMutualFriends(long userId, long friendId) {
        log.debug("userId {} getMutualFriends {}", userId, friendId);
        return userStorage.findAllMutualFriends(getUserById(userId).getId(), getUserById(friendId).getId());
    }

    private User getUserById(long id) {
        return userStorage.findOneById(id).orElseThrow();
    }
}
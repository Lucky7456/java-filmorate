package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage storage;

    public Collection<User> findAll() {
        return storage.findAll();
    }

    public User create(User user) {
        log.debug("create {}", user);
        return storage.create(user);
    }

    public User update(User user) {
        log.debug("update {}", user);
        return storage.update(user);
    }

    public boolean delete(User user) {
        log.debug("delete {}", user);
        return storage.delete(user);
    }

    public void addFriend(long userId, long friendId) {
        storage.addFriend(userId, friendId);
        log.debug("{} addFriend {}", storage.getUserById(userId), storage.getUserById(friendId));
    }

    public void removeFriend(long userId, long friendId) {
        storage.removeFriend(userId, friendId);
        log.debug("{} removeFriend {}", storage.getUserById(userId), storage.getUserById(friendId));
    }

    public Collection<User> findFriends(long userId) {
        log.debug("findFriends {}", storage.getUserById(userId));
        return storage.findAllFriends(userId);
    }

    public Collection<User> getMutualFriends(long userId, long friendId) {
        log.debug("{} getMutualFriends {}", storage.getUserById(userId), storage.getUserById(friendId));
        return storage.findAllMutualFriends(userId,friendId);
    }
}

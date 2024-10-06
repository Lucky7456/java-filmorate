package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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
        user.setId(storage.create(user));
        return user;
    }

    public User update(User user) {
        log.debug("update {}", user);
        if (!storage.update(
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday(),
                user.getId()
        )) {
            throw new NotFoundException("user not found");
        }
        return user;
    }

    public void addFriend(long userId, long friendId) {
        log.debug("userId {} addFriend {}", userId, friendId);
        storage.addFriend(userId, friendId);
    }

    public void removeFriend(long userId, long friendId) {
        log.debug("userId {} removeFriend {}", userId, friendId);
        storage.removeFriend(userId, friendId);
    }

    public Collection<User> findFriends(long userId) {
        log.debug("findFriends {}", userId);
        User user = storage.findOneById(userId)
                .orElseThrow(() -> new NotFoundException("user not found"));
        return storage.findAllById(user.getId());
    }

    public Collection<User> getMutualFriends(long userId, long friendId) {
        log.debug("userId {} getMutualFriends {}", userId, friendId);
        return storage.findAllMutualFriends(userId,friendId);
    }
}

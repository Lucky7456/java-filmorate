package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage storage;

    public Collection<User> findAll() {
        return storage.findAll();
    }

    public User create(User user) {
        return storage.create(user);
    }

    public User update(User user) {
        return storage.update(user);
    }

    public User delete(User user) {
        return storage.delete(user);
    }

    public void addFriend(long userId, long friendId) {
        storage.getUserById(friendId).addFriend(userId);
        storage.getUserById(userId).addFriend(friendId);
    }

    public void removeFriend(long userId, long friendId) {
        storage.getUserById(friendId).removeFriend(userId);
        storage.getUserById(userId).removeFriend(friendId);
    }

    public List<User> findFriends(long userId) {
        return storage.getUserById(userId).getFriends().stream()
                .map(storage::getUserById)
                .toList();
    }

    public List<User> getMutualFriends(long userId, long friendId) {
        return storage.getUserById(userId).getFriends().stream()
                .filter(storage.getUserById(friendId).getFriends()::contains)
                .map(storage::getUserById)
                .toList();
    }
}

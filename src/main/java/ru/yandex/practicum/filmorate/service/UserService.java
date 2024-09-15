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

    public void addFriend(User user, User friend) {
        friend.addFriend(user);
        user.addFriend(friend);
    }

    public void removeFriend(User user, User friend) {
        friend.removeFriend(user);
        user.removeFriend(friend);
    }

    public List<User> getMutualFriends(User user, User friend) {
        return user.getFriends().stream()
                .filter(friend.getFriends()::contains)
                .map(storage::getUserById)
                .toList();
    }
}

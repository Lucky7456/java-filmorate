package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    UserStorage storage;

    void addFriend(User user, User friend) {
        friend.addFriend(user);
        user.addFriend(friend);
    }

    void removeFriend(User user, User friend) {
        friend.removeFriend(user);
        user.removeFriend(friend);
    }

    List<User> getMutualFriends(User user, User friend) {
        return user.getFriends().stream()
                .filter(friend.getFriends()::contains)
                .map(storage::getUserById)
                .toList();
    }
}

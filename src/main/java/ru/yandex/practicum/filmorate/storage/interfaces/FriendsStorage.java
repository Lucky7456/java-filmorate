package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsStorage {
    List<User> findAllMutualFriends(long userId, long otherId);

    int addFriend(long userId, long friendId);

    int removeFriend(long userId, long friendId);
}
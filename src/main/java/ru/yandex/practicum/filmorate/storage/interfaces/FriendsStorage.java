package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FriendsStorage {
    Collection<User> findAllMutualFriends(long userId, long otherId);
    
    void addFriend(long userId, long friendId);
    
    void removeFriend(long userId, long friendId);
}

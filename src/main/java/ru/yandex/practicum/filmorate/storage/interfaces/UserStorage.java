package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    Collection<User> findAll();
    
    Collection<User> findAllFriends(long userId);
    
    Collection<User> findAllMutualFriends(long userId, long otherId);

    User create(User user);

    User update(User user);

    boolean delete(User user);
    
    boolean addFriend(long userId, long friendId);
    
    boolean removeFriend(long userId, long friendId);

    Optional<User> getUserById(long id);
}

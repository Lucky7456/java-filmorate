package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.util.interfaces.CollectionStorage;
import ru.yandex.practicum.filmorate.storage.util.interfaces.CrudStorage;

import java.util.List;

public interface UserStorage extends
        CollectionStorage<User>,
        CrudStorage<User> {
    List<User> findAllMutualFriends(long userId, long otherId);
}
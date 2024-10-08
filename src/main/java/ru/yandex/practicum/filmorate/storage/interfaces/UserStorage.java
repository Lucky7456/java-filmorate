package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage extends
        CollectionStorage<User>,
        CrudStorage<User>,
        FriendsStorage {
}

package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.storage.util.interfaces.CollectionStorage;
import ru.yandex.practicum.filmorate.storage.util.interfaces.CrudStorage;

public interface FeedStorage extends
        CollectionStorage<Feed>,
        CrudStorage<Feed> {
}

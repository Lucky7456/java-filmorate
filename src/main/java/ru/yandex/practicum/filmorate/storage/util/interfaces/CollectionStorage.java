package ru.yandex.practicum.filmorate.storage.util.interfaces;

import java.util.List;

public interface CollectionStorage<T> extends EntityStorage<T> {
    List<T> findAllBy(Object... params);
}

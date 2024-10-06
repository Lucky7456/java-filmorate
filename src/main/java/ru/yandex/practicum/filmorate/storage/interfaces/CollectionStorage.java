package ru.yandex.practicum.filmorate.storage.interfaces;

import java.util.List;

public interface CollectionStorage<T> extends EntityStorage<T> {
    List<T> findAllBy(long id);
}

package ru.yandex.practicum.filmorate.storage.interfaces;

import java.util.Collection;

public interface CollectionStorage<T> extends EntityStorage<T> {
    Collection<T> findAllBy(long id);
}

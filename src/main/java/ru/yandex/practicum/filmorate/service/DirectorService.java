package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.DirectorStorage;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectorService {
    private final DirectorStorage storage;

    public List<Director> findAll() {
        return storage.findAll();
    }

    public Director getDirectorById(long id) {
        log.debug("getDirectorById {}", id);
        return storage.findOneById(id).orElseThrow();
    }

    public Director create(Director director) {
        log.debug("director create {}", director);
        return new Director(storage.create(director), director.name());
    }

    public Director update(Director director) {
        log.debug("director update {}", director);
        int updatedRows = storage.update(
                director.name(),
                director.id()
        );
        if (updatedRows == 1) {
            return director;
        }
        throw new NoSuchElementException("director not found");
    }

    public void delete(long id) {
        log.debug("director delete {}", id);
        storage.delete(id);
    }
}

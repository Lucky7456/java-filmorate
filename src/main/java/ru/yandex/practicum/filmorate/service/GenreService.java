package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage storage;
    
    public Collection<Genre> findAll() {
        return storage.findAll();
    }
    
    public Genre getGenreById(int id) {
        log.debug("getGenreById {}", id);
        return storage.findOneById(id)
                .orElseThrow(() -> new NotFoundException("genre not found"));
    }
}

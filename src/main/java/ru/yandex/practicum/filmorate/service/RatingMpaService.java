package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.interfaces.RatingMpaStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingMpaService {
    private final RatingMpaStorage storage;

    public List<RatingMpa> findAll() {
        return storage.findAll();
    }

    public RatingMpa getRatingMpaById(long id) {
        log.debug("getRatingMpaById {}", id);
        return storage.findOneById(id)
                .orElseThrow(() -> new NotFoundException("ratingMpa not found"));
    }
}
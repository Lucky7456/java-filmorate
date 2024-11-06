package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.interfaces.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final LikesStorage likesStorage;
    private final FilmGenresStorage filmGenresStorage;
    private final RatingMpaStorage ratingMpaStorage;
    private final UserStorage userStorage;

    public List<FilmDto.Response.Public> findAll() {
        return prepare(filmStorage.findAll());
    }

    public List<FilmDto.Response.Public> getMostPopularFilms(int count) {
        log.debug("popular films count {}", count);
        return prepare(filmStorage.findAllBy(count));
    }

    public List<FilmDto.Response.Public> getCommonFilms(long userId, long friendId) {
        log.debug("{} common films {}", userId, friendId);
        return prepare(filmStorage.findCommon(getUserById(userId).getId(), getUserById(friendId).getId()));
    }

    public FilmDto.Response.Public create(FilmDto.Request.Create request) {
        log.debug("create {}", request);
        Film film = FilmMapper.mapToFilm(request);
        film.setId(filmStorage.create(film));
        return process(film, request.getGenres());
    }

    public FilmDto.Response.Public update(FilmDto.Request.Update request) {
        log.debug("update {}", request);
        int updatedRows = filmStorage.update(
                request.getName(),
                request.getDescription(),
                request.getReleaseDate(),
                request.getDuration(),
                request.getMpa().id(),
                request.getId()
        );
        if (updatedRows == 1) {
            filmGenresStorage.delete(request.getId());
            return process(FilmMapper.mapToFilm(request), request.getGenres());
        }
        throw new NoSuchElementException("film not found");
    }

    public void addLike(long filmId, long userId) {
        log.debug("{} addLike {}", filmId, userId);
        likesStorage.insert(filmId, userId);
    }

    public void removeLike(long filmId, long userId) {
        log.debug("{} removeLike {}", filmId, userId);
        likesStorage.delete(filmId, userId);
    }

    public FilmDto.Response.Public getFilmById(long id) {
        log.debug("getFilmById {}", id);
        return prepare(filmStorage.findOneById(id).orElseThrow());
    }

    private FilmDto.Response.Public process(Film film, Set<GenreDto> requestGenres) {
        if (Objects.nonNull(requestGenres)) {
            filmGenresStorage.batchUpdate(requestGenres.stream()
                    .map(genre -> new Object[]{film.getId(), genre.id()})
                    .toList());
        }
        return prepare(film);
    }

    private FilmDto.Response.Public prepare(Film film) {
        RatingMpa rating = ratingMpaStorage.findOneById(film.getMpa()).orElseThrow();
        Set<Genre> genres = new LinkedHashSet<>(genreStorage.findAllBy(film.getId()));
        return FilmMapper.mapToFilmDto(film, rating, genres);
    }

    private List<FilmDto.Response.Public> prepare(List<Film> films) {
        if (films.isEmpty()) {
            return Collections.emptyList();
        }

        List<RatingMpa> ratings = ratingMpaStorage.findAll();
        List<FilmGenre> filmGenres = filmGenresStorage.findAll();
        List<Genre> genres = genreStorage.findAll();

        return films.stream()
                .map(film -> FilmMapper.mapToFilmDto(film,
                        ratings.stream()
                                .filter(rating -> rating.id().equals(film.getMpa()))
                                .findAny().orElseThrow(),
                        filmGenres.stream()
                                .filter(filmGenre -> filmGenre.filmId().equals(film.getId()))
                                .map(filmGenre -> genres.stream()
                                        .filter(genre -> genre.id().equals(filmGenre.genreId()))
                                        .findAny().orElseThrow())
                                .collect(Collectors.toSet())))
                .toList();
    }

    private User getUserById(long id) {
        return userStorage.findOneById(id).orElseThrow();
    }
}

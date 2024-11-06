package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
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
    private final DirectorStorage directorStorage;
    private final FilmDirectorStorage filmDirectorStorage;
    private final LikesStorage likesStorage;
    private final FilmGenresStorage filmGenresStorage;
    private final RatingMpaStorage ratingMpaStorage;

    public List<FilmDto.Response.Public> findAll() {
        return prepare(filmStorage.findAll());
    }

    public List<FilmDto.Response.Public> findSorted(long id, String sortBy) {
        log.debug("films with director {} sorted by {}", id, sortBy);
        return prepare(filmStorage.findSorted(directorStorage.findOneById(id).orElseThrow().id(), sortBy));
    }

    public List<FilmDto.Response.Public> getMostPopularFilms(int count) {
        log.debug("popular films count {}", count);
        return prepare(filmStorage.findAllBy(count));
    }

    public FilmDto.Response.Public create(FilmDto.Request.Create request) {
        log.debug("create {}", request);
        Film film = FilmMapper.mapToFilm(request);
        film.setId(filmStorage.create(film));
        return process(film, request.getGenres(), request.getDirectors());
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
            filmDirectorStorage.delete(request.getId());
            return process(FilmMapper.mapToFilm(request), request.getGenres(), request.getDirectors());
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

    private FilmDto.Response.Public process(Film film, Set<GenreDto> requestGenres, Set<DirectorDto> requestDirectors) {
        if (Objects.nonNull(requestGenres)) {
            filmGenresStorage.batchUpdate(requestGenres.stream()
                    .map(genre -> new Object[]{film.getId(), genre.id()})
                    .toList());
        }
        if (Objects.nonNull(requestDirectors)) {
            filmDirectorStorage.batchUpdate(requestDirectors.stream()
                    .map(director -> new Object[]{film.getId(), director.id()})
                    .toList());
        }
        return prepare(film);
    }

    private FilmDto.Response.Public prepare(Film film) {
        RatingMpa rating = ratingMpaStorage.findOneById(film.getMpa()).orElseThrow();
        Set<Genre> genres = new LinkedHashSet<>(genreStorage.findAllBy(film.getId()));
        Set<Director> directors = new LinkedHashSet<>(directorStorage.findAllBy(film.getId()));
        return FilmMapper.mapToFilmDto(film, rating, genres, directors);
    }

    private List<FilmDto.Response.Public> prepare(List<Film> films) {
        if (films.isEmpty()) {
            return Collections.emptyList();
        }

        List<RatingMpa> ratings = ratingMpaStorage.findAll();
        List<FilmGenre> filmGenres = filmGenresStorage.findAll();
        List<Genre> genres = genreStorage.findAll();
        List<FilmDirector> filmDirectors = filmDirectorStorage.findAll();
        List<Director> directors = directorStorage.findAll();

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
                                .collect(Collectors.toSet()),
                        filmDirectors.stream()
                                .filter(filmDirector -> filmDirector.filmId().equals(film.getId()))
                                .map(filmDirector -> directors.stream()
                                        .filter(director -> director.id().equals(filmDirector.directorId()))
                                        .findAny().orElseThrow())
                                .collect(Collectors.toSet())))
                .toList();
    }
}

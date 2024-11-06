package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService service;

    @GetMapping
    public List<FilmDto.Response.Public> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public FilmDto.Response.Public getFilm(@PathVariable long id) {
        return service.getFilmById(id);
    }

    @PostMapping
    public FilmDto.Response.Public create(@Valid @RequestBody FilmDto.Request.Create film) {
        return service.create(film);
    }

    @PutMapping
    public FilmDto.Response.Public update(@Valid @RequestBody FilmDto.Request.Update film) {
        return service.update(film);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        service.delete(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void like(@PathVariable long id, @PathVariable long userId) {
        service.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void dislike(@PathVariable long id, @PathVariable long userId) {
        service.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<FilmDto.Response.Public> popular(@RequestParam(defaultValue = "10") int count) {
        return service.getMostPopularFilms(count);
    }

    @GetMapping("/common")
    public List<FilmDto.Response.Public> common(@RequestParam long userId, @RequestParam long friendId) {
        return service.getCommonFilms(userId, friendId);
    }
}

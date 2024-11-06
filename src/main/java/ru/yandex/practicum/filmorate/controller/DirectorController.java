package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.List;

@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorService service;

    @GetMapping
    public List<Director> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Director getDirector(@PathVariable long id) {
        return service.getDirectorById(id);
    }

    @PostMapping
    public Director create(@Valid @RequestBody Director director) {
        return service.create(director);
    }

    @PutMapping
    public Director update(@Valid @RequestBody Director director) {
        return service.update(director);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        service.delete(id);
    }
}

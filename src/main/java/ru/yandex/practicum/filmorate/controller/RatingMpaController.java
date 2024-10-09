package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.service.RatingMpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class RatingMpaController {
    private final RatingMpaService service;

    @GetMapping
    public List<RatingMpa> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public RatingMpa getRatingMpaById(@PathVariable long id) {
        return service.getRatingMpaById(id);
    }
}
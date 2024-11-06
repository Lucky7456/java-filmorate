package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;

public record Director(Long id, @NotBlank String name) {
}

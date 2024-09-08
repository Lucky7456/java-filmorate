package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Data
public class User {
    Long id;
    @NotBlank
    @Email
    String email;
    @NotNull
    @Pattern(regexp = "\\S+")
    String login;
    @Getter(AccessLevel.NONE)
    String name;
    @Past
    LocalDate birthday;

    public String getName() {
        return name == null || name.isBlank() ? login : name;
    }
}

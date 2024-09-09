package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Data
public class User {
    private Long id;
    @NotBlank
    @Email
    private String email;
    @NotNull
    @Pattern(regexp = "\\S+")
    private String login;
    @Getter(AccessLevel.NONE)
    private String name;
    @Past
    private LocalDate birthday;

    public String getName() {
        return name == null || name.isBlank() ? login : name;
    }
}

package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    @Getter(AccessLevel.NONE)
    private String name;
    @NotNull
    @Pattern(regexp = "\\S+")
    private String login;
    @NotBlank
    @Email
    private String email;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;

    public String getName() {
        return name == null || name.isBlank() ? login : name;
    }
}

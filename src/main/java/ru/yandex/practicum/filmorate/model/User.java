package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    @NotNull
    @PastOrPresent
    private LocalDate birthday;

    private final Set<Long> friends = new HashSet<>();

    public void addFriend(Long friendId) {
        friends.add(friendId);
    }

    public void removeFriend(Long friendId) {
        friends.remove(friendId);
    }

    public String getName() {
        return name == null || name.isBlank() ? login : name;
    }
}

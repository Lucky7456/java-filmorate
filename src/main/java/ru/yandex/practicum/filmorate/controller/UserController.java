package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private long nextId = 0;

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.debug("create {}", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.debug("update {}", user);
            return user;
        }
        throw new ValidationException("Пользователь с id = " + user.getId() + " не найден");
    }

    private long getNextId() {
        return ++nextId;
    }
}

package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SQLDataException extends RuntimeException {
    public SQLDataException(String message) {
        super(message);
        log.warn(message);
    }
}

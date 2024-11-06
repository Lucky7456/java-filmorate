package ru.yandex.practicum.filmorate.model;

import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.Operation;

public record Feed(Long eventId, Long userId, EventType eventType, Operation operation, Long entityId, Long timestamp) {
}

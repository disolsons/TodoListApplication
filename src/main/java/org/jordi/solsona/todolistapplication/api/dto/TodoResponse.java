package org.jordi.solsona.todolistapplication.api.dto;

import org.jordi.solsona.todolistapplication.domain.model.TodoStatus;

import java.time.Instant;
import java.util.UUID;

public record TodoResponse(
        UUID id,
        String name,
        String description,
        Instant dueDate,
        TodoStatus status
) {}

package org.jordi.solsona.todolistapplication.api.dto;

import org.jordi.solsona.todolistapplication.domain.model.TodoListStatus;

import java.time.Instant;
import java.util.UUID;

public record TodoListResponse(
        UUID id,
        String name,
        String description,
        Instant dueDate,
        TodoListStatus status
) {}

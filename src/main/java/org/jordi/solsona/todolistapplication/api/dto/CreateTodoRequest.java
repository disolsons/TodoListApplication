package org.jordi.solsona.todolistapplication.api.dto;

import jakarta.validation.constraints.NotBlank;
import org.jordi.solsona.todolistapplication.domain.model.TodoStatus;

import java.time.Instant;

public record CreateTodoRequest(
        @NotBlank String name,
        String description,
        Instant dueDate,
        TodoStatus status
) {}
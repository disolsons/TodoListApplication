package org.jordi.solsona.todolistapplication.api.dto;

import jakarta.validation.constraints.NotBlank;
import org.jordi.solsona.todolistapplication.domain.model.TodoListStatus;

import java.time.Instant;

public record  UpdateTodoListRequest(
        @NotBlank String name,
        String description,
        Instant dueDate,
        TodoListStatus status
) {}

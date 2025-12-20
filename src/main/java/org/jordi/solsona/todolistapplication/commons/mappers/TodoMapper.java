package org.jordi.solsona.todolistapplication.commons.mappers;

import org.jordi.solsona.todolistapplication.api.dto.CreateTodoRequest;
import org.jordi.solsona.todolistapplication.api.dto.TodoResponse;
import org.jordi.solsona.todolistapplication.domain.model.Todo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TodoMapper {

    Todo toEntity(CreateTodoRequest request);

    TodoResponse toResponse(Todo todo);
}

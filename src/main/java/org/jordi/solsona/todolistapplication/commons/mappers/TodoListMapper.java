package org.jordi.solsona.todolistapplication.commons.mappers;

import org.jordi.solsona.todolistapplication.api.dto.CreateTodoListRequest;
import org.jordi.solsona.todolistapplication.api.dto.TodoListResponse;
import org.jordi.solsona.todolistapplication.domain.model.TodoList;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TodoListMapper {

    TodoList toEntity(CreateTodoListRequest request);

    TodoListResponse toResponse(TodoList todoList);
}

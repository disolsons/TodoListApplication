package org.jordi.solsona.todolistapplication.api.controller;
import jakarta.validation.Valid;
import org.jordi.solsona.todolistapplication.api.dto.CreateTodoListRequest;
import org.jordi.solsona.todolistapplication.api.dto.TodoListResponse;
import org.jordi.solsona.todolistapplication.api.dto.UpdateTodoListRequest;
import org.jordi.solsona.todolistapplication.commons.mappers.TodoListMapper;
import org.jordi.solsona.todolistapplication.domain.model.TodoList;
import org.jordi.solsona.todolistapplication.domain.model.TodoListStatus;
import org.jordi.solsona.todolistapplication.service.TodoListService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/lists")
public class TodoListController {

    private final TodoListService todoListService;
    private final TodoListMapper mapper;

    public TodoListController(TodoListService todoListService, TodoListMapper mapper) {
        this.todoListService = todoListService;
        this.mapper = mapper;
    }

    @PostMapping
    public TodoListResponse create(@RequestBody @Valid CreateTodoListRequest request) {
        TodoList response = this.todoListService.createTodoList(request);
        return mapper.toResponse(response);
    }

    @GetMapping("/{id}")
    public TodoListResponse get(@PathVariable UUID id) {
        TodoList response = this.todoListService.getTodoListById(id);
        return mapper.toResponse(response);
    }

    @GetMapping
    public Page<TodoListResponse> list(@RequestParam(required = false) TodoListStatus status,
                               @RequestParam(required = false) Instant dueTime,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "dueDate") String sortBy,
                               @RequestParam(defaultValue = "ASC") Sort.Direction orderDirection) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(orderDirection, sortBy));
        Page<TodoList> todoListPage = todoListService.list(status, dueTime, pageable);

        return todoListPage.map(mapper::toResponse);
    }

    @PutMapping
    public TodoListResponse update(@PathVariable UUID id, @RequestBody @Valid UpdateTodoListRequest request) {
        TodoList response =  this.todoListService.update(id, request);
        return mapper.toResponse(response);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        this.todoListService.delete(id);
    }
}

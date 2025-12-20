package org.jordi.solsona.todolistapplication.api.controller;
import jakarta.validation.Valid;
import org.jordi.solsona.todolistapplication.api.dto.CreateTodoRequest;
import org.jordi.solsona.todolistapplication.api.dto.TodoResponse;
import org.jordi.solsona.todolistapplication.api.dto.UpdateTodoRequest;
import org.jordi.solsona.todolistapplication.commons.mappers.TodoMapper;
import org.jordi.solsona.todolistapplication.domain.model.Todo;
import org.jordi.solsona.todolistapplication.domain.model.TodoStatus;
import org.jordi.solsona.todolistapplication.service.TodoListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/lists")
public class TodoListController {

    private final TodoListService todoListService;
    private final TodoMapper mapper;

    @Autowired
    public TodoListController(TodoListService todoListService, TodoMapper mapper) {
        this.todoListService = todoListService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<TodoResponse> create(@RequestBody @Valid CreateTodoRequest request) {
        Todo todo = this.todoListService.createTodo(request);
        TodoResponse response = this.mapper.toResponse(todo);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoResponse> get(@PathVariable UUID id) {
        Todo response = this.todoListService.getTodoById(id);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapper.toResponse(response));
    }

    @GetMapping
    public  ResponseEntity<Page<TodoResponse>> list(@RequestParam(required = false) TodoStatus status,
                                                    @RequestParam(required = false) Instant dueTime,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(defaultValue = "dueDate") String sortBy,
                                                    @RequestParam(defaultValue = "ASC") Sort.Direction orderDirection) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(orderDirection, sortBy));
        Page<Todo> todoListPage = this.todoListService.list(status, dueTime, pageable);

        return ResponseEntity.ok(todoListPage.map(this.mapper::toResponse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoResponse> update(@PathVariable UUID id, @RequestBody @Valid UpdateTodoRequest request) {
        Todo response =  this.todoListService.update(id, request);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapper.toResponse(response));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        this.todoListService.delete(id);
    }
}

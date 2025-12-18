package org.jordi.solsona.todolistapplication.api.controller;
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

    public TodoListController(TodoListService todoListService) {
        this.todoListService = todoListService;
    }

    @PostMapping
    public TodoList create(@RequestBody TodoList todoList) {
        return this.todoListService.createTodoList(todoList);
    }

    @GetMapping("/{id}")
    public TodoList get(@PathVariable UUID id) {
        return this.todoListService.getTodoListById(id);
    }

    @GetMapping
    public Page<TodoList> list(@RequestParam(required = false) TodoListStatus status,
                               @RequestParam(required = false) Instant dueTime,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "dueDate") String sortBy,
                               @RequestParam(defaultValue = "ASC") Sort.Direction orderDirection) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(orderDirection, sortBy));
        return this.todoListService.list(status, dueTime, pageable);
    }

    @PutMapping
    public TodoList update(@PathVariable UUID id, @RequestBody TodoList todoList) {
        return this.todoListService.update(id, todoList);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        this.todoListService.delete(id);
    }
}

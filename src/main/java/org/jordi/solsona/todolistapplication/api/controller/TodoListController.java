package org.jordi.solsona.todolistapplication.api.controller;
import org.jordi.solsona.todolistapplication.domain.model.TodoList;
import org.jordi.solsona.todolistapplication.service.TodoListService;
import org.springframework.web.bind.annotation.*;

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
        return todoListService.createList(todoList);
    }

    @GetMapping("/{id}")
    public TodoList get(@PathVariable UUID id) {
        return todoListService.getListById(id);
    }
}

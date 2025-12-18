package org.jordi.solsona.todolistapplication.service;

import org.jordi.solsona.todolistapplication.commons.exceptions.ListNotFoundException;
import org.jordi.solsona.todolistapplication.domain.model.TodoList;
import org.jordi.solsona.todolistapplication.domain.repository.TodoListRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TodoListService {

    private final TodoListRepository listRepo;

    public TodoListService(TodoListRepository listRepo) {
        this.listRepo = listRepo;
    }

    /**
     * Creates a new list
     * @param todoList the {@link TodoList} to be created
     * @return the created {@link TodoList}
     */
    public TodoList createList(TodoList todoList) {
        return this.listRepo.save(todoList);
    }

    /**
     * Get a list by its id.
     * @param id the list id
     * @return the {@link TodoList} with the parameter id.
     */
    public TodoList getListById(UUID id) {
        return this.listRepo.findById(id).orElseThrow(() -> new ListNotFoundException(id));
    }
}

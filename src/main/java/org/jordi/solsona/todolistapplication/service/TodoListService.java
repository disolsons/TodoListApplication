package org.jordi.solsona.todolistapplication.service;

import org.jordi.solsona.todolistapplication.domain.model.TodoListStatus;
import org.jordi.solsona.todolistapplication.domain.repository.TodoListSpecifications;
import org.springframework.data.domain.Page;
import org.jordi.solsona.todolistapplication.commons.exceptions.ListNotFoundException;
import org.jordi.solsona.todolistapplication.domain.model.TodoList;
import org.jordi.solsona.todolistapplication.domain.repository.TodoListRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
    public TodoList createTodoList(TodoList todoList) {
        return this.listRepo.save(todoList);
    }

    /**
     * Get a list by its id.
     * @param id the list id
     * @return the {@link TodoList} with the parameter id.
     */
    public TodoList getTodoListById(UUID id) {
        return this.listRepo.findById(id).orElseThrow(() -> new ListNotFoundException(id));
    }

    /**
     * @param status
     * @param dueTime
     * @param pageable
     * @return all the {@link TodoList} matching the criteria.
     */
    public Page<TodoList> list(TodoListStatus status, Instant dueTime, Pageable pageable) {

        Specification<TodoList> specification = Specification
                .where(TodoListSpecifications.hasStatus(status)).and(TodoListSpecifications.dueBefore(dueTime));

        return this.listRepo.findAll(specification, pageable);
    }

    /**
     *
     * @param id
     */
    public void delete(UUID id) {
        this.listRepo.deleteById(id);
    }

    /**
     *
     * @param id
     * @param updated
     * @return the updated {@link TodoList}
     */
    public TodoList update(UUID id, TodoList updated) {
        TodoList toBeUpdated = getTodoListById(id);
        toBeUpdated.setName(updated.getName());
        toBeUpdated.setDescription(updated.getDescription());
        toBeUpdated.setDueDate(updated.getDueDate());
        toBeUpdated.setStatus(updated.getStatus());

        return this.listRepo.save(toBeUpdated);
    }
}

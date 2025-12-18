package org.jordi.solsona.todolistapplication.domain.repository;

import org.jordi.solsona.todolistapplication.domain.model.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface TodoListRepository extends JpaRepository<TodoList, UUID>, JpaSpecificationExecutor<TodoList> {
}

package org.jordi.solsona.todolistapplication.domain.repository;

import org.jordi.solsona.todolistapplication.domain.model.TodoList;
import org.jordi.solsona.todolistapplication.domain.model.TodoListStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

public class TodoListSpecifications {

    /**
     * Creates a {@link Specification} that restricts {@link TodoList} results those with the {@link TodoListStatus} matching the parameter.
     * @param status the status to filter by
     * @return a {@link Specification} that matches {@link TodoList} with the given status.
     */
    public static Specification<TodoList> hasStatus(TodoListStatus status) {
        return (root, query, criteriaBuilder) ->
                    status == null ? null : criteriaBuilder.equal(root.get("status"), status);
    }

    /**
     * Creates a {@link Specification} that restricts {@link TodoList} results those with a dueDate earlier than the parameter
     * @param timestamp the timestamp to filter by
     * @return a {@link Specification} that matches {@link TodoList} with the given criteria.
     */
    public static Specification<TodoList> dueBefore(Instant timestamp) {
        return (root, query, criteriaBuilder) ->
                    timestamp == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get("dueDate"), timestamp);
    }

}

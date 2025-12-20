package org.jordi.solsona.todolistapplication.domain.repository;

import org.jordi.solsona.todolistapplication.domain.model.Todo;
import org.jordi.solsona.todolistapplication.domain.model.TodoStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

public class TodoSpecifications {

    /**
     * Creates a {@link Specification} that restricts {@link Todo} results those with the {@link TodoStatus} matching the parameter.
     * @param status the status to filter by
     * @return a {@link Specification} that matches {@link Todo} with the given status.
     */
    public static Specification<Todo> hasStatus(TodoStatus status) {
        return (root, query, criteriaBuilder) ->
                    status == null ? null : criteriaBuilder.equal(root.get("status"), status);
    }

    /**
     * Creates a {@link Specification} that restricts {@link Todo} results those with a dueDate earlier than the parameter
     * @param timestamp the timestamp to filter by
     * @return a {@link Specification} that matches {@link Todo} with the given criteria.
     */
    public static Specification<Todo> dueBefore(Instant timestamp) {
        return (root, query, criteriaBuilder) ->
                    timestamp == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get("dueDate"), timestamp);
    }

}

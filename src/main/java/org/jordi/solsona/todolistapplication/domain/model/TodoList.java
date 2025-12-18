package org.jordi.solsona.todolistapplication.domain.model;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "lists")
public class TodoList {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private TodoListStatus status;

    private Instant dueDate;

    private Instant createdAt;

    private Instant updatedAt;


    /***
     * Getters and Setters
     */

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TodoListStatus getStatus() {
        return status;
    }

    public void setStatus(TodoListStatus status) {
        this.status = status;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}

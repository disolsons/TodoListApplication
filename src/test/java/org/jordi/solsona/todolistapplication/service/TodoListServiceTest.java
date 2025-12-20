package org.jordi.solsona.todolistapplication.service;

import org.jordi.solsona.todolistapplication.api.dto.CreateTodoRequest;
import org.jordi.solsona.todolistapplication.api.dto.UpdateTodoRequest;
import org.jordi.solsona.todolistapplication.commons.exceptions.TodoNotFoundException;
import org.jordi.solsona.todolistapplication.commons.mappers.TodoMapper;
import org.jordi.solsona.todolistapplication.domain.model.Todo;
import org.jordi.solsona.todolistapplication.domain.model.TodoStatus;
import org.jordi.solsona.todolistapplication.domain.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TodoListServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoListService todoListService;

    @Mock
    private TodoMapper todoMapper;

    private Todo todo;
    private CreateTodoRequest createRequest;
    private UpdateTodoRequest updateRequest;
    private UUID uuid = new UUID(0L, 1L);
    private Instant currentTime = Instant.now();

    @BeforeEach
    public void setUp() {
        this.todo = new Todo();
        this.todo.setId(this.uuid);
        this.todo.setName("testTodoList");
        this.todo.setDescription("A description");
        this.todo.setDueDate(currentTime);
        this.todo.setStatus(TodoStatus.NOT_STARTED);

        this.createRequest = new CreateTodoRequest("testTodoList", "A description", currentTime, TodoStatus.NOT_STARTED);
        this.updateRequest = new UpdateTodoRequest("testTodoList", "A description", currentTime, TodoStatus.IN_PROGRESS);
    }

    @Test
    public void getById_todoExists_returnsTodoList() {

        when(this.todoRepository.findById(this.uuid))
                .thenReturn(Optional.of(this.todo));

        Todo returnedList = this.todoListService.getTodoById(this.uuid);
        assertThat(returnedList).isSameAs(this.todo);
        verify(this.todoRepository).findById(this.uuid);
    }

    @Test
    public void getById_todoDoesntExist_throwsException() {

        when(this.todoRepository.findById(this.uuid))
                .thenReturn(Optional.empty());

        assertThrows(TodoNotFoundException.class, () -> {
            this.todoListService.getTodoById(this.uuid);
        });
        verify(this.todoRepository).findById(this.uuid);
    }

    @Test
    public void create_savesAndReturnsTodo() {

        when(this.todoMapper.toEntity(this.createRequest)).thenReturn(this.todo);
        when(this.todoRepository.save(any(Todo.class))).thenReturn(this.todo);
        Todo createdList = this.todoListService.createTodo(this.createRequest);

        assertThat(createdList).isSameAs(this.todo);
        verify(this.todoRepository).save(any(Todo.class));
    }

    @Test
    public void update_existingTodo_udpatesFieldsAndSavesList() {

        Todo updatedList = this.todo;
        updatedList.setStatus(TodoStatus.IN_PROGRESS);

        when(this.todoRepository.findById(this.uuid)).thenReturn(Optional.of(this.todo));
        when(this.todoRepository.save(any(Todo.class))).thenReturn(updatedList);

        Todo result = this.todoListService.update(this.uuid, this.updateRequest);


        assertThat(result.getName()).isEqualTo("testTodoList");
        assertThat(result.getDescription()).isEqualTo("A description");
        assertThat(result.getDueDate()).isEqualTo(updatedList.getDueDate());
        assertThat(result.getStatus()).isEqualTo(TodoStatus.IN_PROGRESS);

        verify(this.todoRepository).save(any(Todo.class));
    }

    @Test
    public void update_missingTodo_throwsException() {

        Todo updatedList = this.todo;
        updatedList.setStatus(TodoStatus.IN_PROGRESS);

        when(this.todoRepository.findById(updatedList.getId()))
                .thenReturn(Optional.empty());

        assertThrows(TodoNotFoundException.class, () -> {
            this.todoListService.update(this.uuid, this.updateRequest);
        });
    }

    @Test
    void delete_missingTodo_throwsException() {

        doThrow(new IllegalArgumentException()).when(this.todoRepository).deleteById(this.uuid);
        assertThrows(TodoNotFoundException.class, () -> {
            this.todoListService.delete(this.uuid);
        });
    }


    @Test
    void list_withFilters_returnsTodoPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Todo> page = new PageImpl<>(List.of(this.todo), pageable, 1);

        when(todoRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        Page<Todo> result = todoListService.list(TodoStatus.NOT_STARTED, null, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isSameAs(this.todo);
    }




}



package org.jordi.solsona.todolistapplication.api;

import org.jordi.solsona.todolistapplication.api.controller.TodoListController;
import org.jordi.solsona.todolistapplication.api.dto.CreateTodoRequest;
import org.jordi.solsona.todolistapplication.api.dto.TodoResponse;
import org.jordi.solsona.todolistapplication.api.dto.UpdateTodoRequest;
import org.jordi.solsona.todolistapplication.commons.mappers.TodoMapper;
import org.jordi.solsona.todolistapplication.domain.model.Todo;
import org.jordi.solsona.todolistapplication.domain.model.TodoStatus;
import org.jordi.solsona.todolistapplication.service.TodoListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class TodoListControllerTest {

    @Mock
    private TodoListService todoListService;

    @Mock
    private TodoMapper todoMapper;

    @InjectMocks
    private TodoListController todoListController;

    private Todo todo;
    private TodoResponse todoResponse;
    private CreateTodoRequest createRequest;
    private UpdateTodoRequest updateRequest;
    private UUID uuid;
    private Instant currentTime;

    @BeforeEach
    public void setUp() {
        this.uuid = UUID.randomUUID();
        this.currentTime = Instant.now();

        // Initialize test objects
        this.todo = new Todo();
        this.todo.setId(uuid);
        this.todo.setName("Test Todo List");
        this.todo.setDescription("Test description");
        this.todo.setDueDate(currentTime);
        this.todo.setStatus(TodoStatus.NOT_STARTED);

        this.todoResponse = new TodoResponse(this.uuid, "Test Todo List", "Test description",this.currentTime, TodoStatus.NOT_STARTED);

        this.createRequest = new CreateTodoRequest("Test Todo List", "Test description", this.currentTime, TodoStatus.NOT_STARTED);
        this.updateRequest = new UpdateTodoRequest("Updated Todo List", "Updated description", this.currentTime, TodoStatus.IN_PROGRESS);
    }

    @Test
    public void create_shouldReturnCreatedTodoResponse() {

        when(this.todoListService.createTodo(any(CreateTodoRequest.class))).thenReturn(this.todo);
        when(this.todoMapper.toResponse(this.todo)).thenReturn(this.todoResponse);

        ResponseEntity<TodoResponse> result = this.todoListController.create(this.createRequest);

        assertThat(result.getBody()).isEqualTo(this.todoResponse);
        verify(this.todoListService).createTodo(any(CreateTodoRequest.class));
        verify(this.todoMapper).toResponse(this.todo);
    }

    @Test
    public void get_shouldReturnTodoResponse() {
        when(this.todoListService.getTodoById(any(UUID.class))).thenReturn(this.todo);
        when(this.todoMapper.toResponse(this.todo)).thenReturn(this.todoResponse);

        ResponseEntity<TodoResponse> result = this.todoListController.get(this.uuid);

        assertThat(result.getBody()).isEqualTo(this.todoResponse);
        verify(this.todoListService).getTodoById(any(UUID.class));
        verify(this.todoMapper).toResponse(this.todo);
    }

    @Test
    public void list_pageParameterIs0_shouldReturnEmptyPagedTodoResponses() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "dueDate"));
        Page<Todo> page = Page.empty(pageable);
        when(this.todoListService.list(any(), any(), eq(pageable))).thenReturn(page);

        ResponseEntity<Page<TodoResponse>> result = this.todoListController.list(null, null, 0, 10, "dueDate", Sort.Direction.ASC);

        assertThat(result.getBody().getContent()).isEmpty();
        verify(this.todoListService).list(any(), any(), eq(pageable));
    }


    @Test
    public void list_shouldReturnPagedTodoResponses() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "dueDate"));
        Page<Todo> page = new PageImpl<>(List.of(this.todo), pageable, 1);
        when(this.todoListService.list(any(), any(), any(Pageable.class))).thenReturn(page);
        when(todoMapper.toResponse(this.todo)).thenReturn(this.todoResponse);

        ResponseEntity<Page<TodoResponse>> result = this.todoListController.list(null, null, 1, 10, "dueDate", Sort.Direction.ASC);

        assertThat(result.getBody().getContent()).isNotEmpty();
        assertThat(result.getBody().getContent().get(0).name()).isEqualTo("Test Todo List");
        verify(this.todoListService).list(any(), any(), any(Pageable.class));
    }


    @Test
    public void update_shouldReturnUpdatedTodoResponse() {
        when(this.todoListService.update(any(UUID.class), any(UpdateTodoRequest.class))).thenReturn(todo);
        when(this.todoMapper.toResponse(this.todo)).thenReturn(this.todoResponse);

        ResponseEntity<TodoResponse> result = this.todoListController.update(this.uuid, this.updateRequest);

        assertThat(result.getBody()).isEqualTo(this.todoResponse);
        verify(this.todoListService).update(any(UUID.class), any(UpdateTodoRequest.class));
        verify(this.todoMapper).toResponse(this.todo);
    }

    @Test
    public void delete_shouldCallDeleteServiceMethod() {
        this.todoListController.delete(this.uuid);

        verify(this.todoListService).delete(any(UUID.class));
    }
}


package org.jordi.solsona.todolistapplication.api;

import org.jordi.solsona.todolistapplication.api.controller.TodoListController;
import org.jordi.solsona.todolistapplication.api.dto.CreateTodoListRequest;
import org.jordi.solsona.todolistapplication.api.dto.TodoListResponse;
import org.jordi.solsona.todolistapplication.api.dto.UpdateTodoListRequest;
import org.jordi.solsona.todolistapplication.commons.mappers.TodoListMapper;
import org.jordi.solsona.todolistapplication.domain.model.TodoList;
import org.jordi.solsona.todolistapplication.domain.model.TodoListStatus;
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
    private TodoListMapper todoListMapper;

    @InjectMocks
    private TodoListController todoListController;

    private TodoList todoList;
    private TodoListResponse todoListResponse;
    private CreateTodoListRequest createRequest;
    private UpdateTodoListRequest updateRequest;
    private UUID listUUID;
    private Instant currentTime;

    @BeforeEach
    public void setUp() {
        this.listUUID = UUID.randomUUID();
        this.currentTime = Instant.now();

        // Initialize test objects
        this.todoList = new TodoList();
        this.todoList.setId(listUUID);
        this.todoList.setName("Test Todo List");
        this.todoList.setDescription("Test description");
        this.todoList.setDueDate(currentTime);
        this.todoList.setStatus(TodoListStatus.NOT_STARTED);

        this.todoListResponse = new TodoListResponse(this.listUUID, "Test Todo List", "Test description",this.currentTime, TodoListStatus.NOT_STARTED);

        this.createRequest = new CreateTodoListRequest("Test Todo List", "Test description", this.currentTime, TodoListStatus.NOT_STARTED);
        this.updateRequest = new UpdateTodoListRequest("Updated Todo List", "Updated description", this.currentTime, TodoListStatus.IN_PROGRESS);
    }

    @Test
    public void create_shouldReturnCreatedTodoListResponse() {

        when(this.todoListService.createTodoList(any(CreateTodoListRequest.class))).thenReturn(this.todoList);
        when(this.todoListMapper.toResponse(this.todoList)).thenReturn(this.todoListResponse);

        ResponseEntity<TodoListResponse> result = this.todoListController.create(this.createRequest);

        assertThat(result.getBody()).isEqualTo(this.todoListResponse);
        verify(this.todoListService).createTodoList(any(CreateTodoListRequest.class));
        verify(this.todoListMapper).toResponse(this.todoList);
    }

    @Test
    public void get_shouldReturnTodoListResponse() {
        when(this.todoListService.getTodoListById(any(UUID.class))).thenReturn(this.todoList);
        when(this.todoListMapper.toResponse(this.todoList)).thenReturn(this.todoListResponse);

        ResponseEntity<TodoListResponse> result = this.todoListController.get(this.listUUID);

        assertThat(result.getBody()).isEqualTo(this.todoListResponse);
        verify(this.todoListService).getTodoListById(any(UUID.class));
        verify(this.todoListMapper).toResponse(this.todoList);
    }

    @Test
    public void list_pageParameterIs0_shouldReturnEmptyPagedTodoListResponses() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "dueDate"));
        Page<TodoList> page = Page.empty(pageable);
        when(this.todoListService.list(any(), any(), eq(pageable))).thenReturn(page);

        ResponseEntity<Page<TodoListResponse>> result = this.todoListController.list(null, null, 0, 10, "dueDate", Sort.Direction.ASC);

        assertThat(result.getBody().getContent()).isEmpty();
        verify(this.todoListService).list(any(), any(), eq(pageable));
    }


    @Test
    public void list_shouldReturnPagedTodoListResponses() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "dueDate"));
        Page<TodoList> page = new PageImpl<>(List.of(this.todoList), pageable, 1);
        when(this.todoListService.list(any(), any(), any(Pageable.class))).thenReturn(page);
        when(todoListMapper.toResponse(this.todoList)).thenReturn(this.todoListResponse);

        ResponseEntity<Page<TodoListResponse>> result = this.todoListController.list(null, null, 1, 10, "dueDate", Sort.Direction.ASC);

        assertThat(result.getBody().getContent()).isNotEmpty();
        assertThat(result.getBody().getContent().get(0).name()).isEqualTo("Test Todo List");
        verify(this.todoListService).list(any(), any(), any(Pageable.class));
    }


    @Test
    public void update_shouldReturnUpdatedTodoListResponse() {
        when(this.todoListService.update(any(UUID.class), any(UpdateTodoListRequest.class))).thenReturn(todoList);
        when(this.todoListMapper.toResponse(this.todoList)).thenReturn(this.todoListResponse);

        ResponseEntity<TodoListResponse> result = this.todoListController.update(this.listUUID, this.updateRequest);

        assertThat(result.getBody()).isEqualTo(this.todoListResponse);
        verify(this.todoListService).update(any(UUID.class), any(UpdateTodoListRequest.class));
        verify(this.todoListMapper).toResponse(this.todoList);
    }

    @Test
    public void delete_shouldCallDeleteServiceMethod() {
        this.todoListController.delete(this.listUUID);

        verify(this.todoListService).delete(any(UUID.class));
    }
}


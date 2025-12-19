package org.jordi.solsona.todolistapplication.service;

import org.jordi.solsona.todolistapplication.api.dto.CreateTodoListRequest;
import org.jordi.solsona.todolistapplication.api.dto.UpdateTodoListRequest;
import org.jordi.solsona.todolistapplication.commons.exceptions.ListNotFoundException;
import org.jordi.solsona.todolistapplication.commons.mappers.TodoListMapper;
import org.jordi.solsona.todolistapplication.domain.model.TodoList;
import org.jordi.solsona.todolistapplication.domain.model.TodoListStatus;
import org.jordi.solsona.todolistapplication.domain.repository.TodoListRepository;
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
    private TodoListRepository todoListRepository;

    @InjectMocks
    private TodoListService todoListService;

    @Mock
    private TodoListMapper todoListMapper;

    private TodoList todoList;
    private CreateTodoListRequest createRequest;
    private UpdateTodoListRequest updateRequest;
    private UUID listUUID = new UUID(0L, 1L);
    private Instant currentTime = Instant.now();

    @BeforeEach
    public void setUp() {
        this.todoList = new TodoList();
        this.todoList.setId(listUUID);
        this.todoList.setName("testTodoList");
        this.todoList.setDescription("A description");
        this.todoList.setDueDate(currentTime);
        this.todoList.setStatus(TodoListStatus.NOT_STARTED);

        this.createRequest = new CreateTodoListRequest("testTodoList", "A description", currentTime, TodoListStatus.NOT_STARTED);
        this.updateRequest = new UpdateTodoListRequest("testTodoList", "A description", currentTime, TodoListStatus.IN_PROGRESS);
    }

    @Test
    public void getById_todoListExists_returnsTodoList() {

        when(this.todoListRepository.findById(this.listUUID))
                .thenReturn(Optional.of(this.todoList));

        TodoList returnedList = this.todoListService.getTodoListById(this.listUUID);
        assertThat(returnedList).isSameAs(this.todoList);
        verify(this.todoListRepository).findById(this.listUUID);
    }

    @Test
    public void getById_todoListDoesntExist_throwsException() {

        when(this.todoListRepository.findById(this.listUUID))
                .thenReturn(Optional.empty());

        assertThrows(ListNotFoundException.class, () -> {
            this.todoListService.getTodoListById(this.listUUID);
        });
        verify(this.todoListRepository).findById(this.listUUID);
    }

    @Test
    public void create_savesAndReturnsTodoList() {

        when(this.todoListMapper.toEntity(this.createRequest)).thenReturn(this.todoList);
        when(this.todoListRepository.save(any(TodoList.class))).thenReturn(this.todoList);
        TodoList createdList = this.todoListService.createTodoList(this.createRequest);

        assertThat(createdList).isSameAs(this.todoList);
        verify(this.todoListRepository).save(any(TodoList.class));
    }

    @Test
    public void update_existingTodoList_udpatesFieldsAndSavesList() {

        TodoList updatedList = this.todoList;
        updatedList.setStatus(TodoListStatus.IN_PROGRESS);

        when(this.todoListRepository.findById(this.listUUID)).thenReturn(Optional.of(this.todoList));
        when(this.todoListRepository.save(any(TodoList.class))).thenReturn(updatedList);

        TodoList result = this.todoListService.update(this.listUUID, this.updateRequest);


        assertThat(result.getName()).isEqualTo("testTodoList");
        assertThat(result.getDescription()).isEqualTo("A description");
        assertThat(result.getDueDate()).isEqualTo(updatedList.getDueDate());
        assertThat(result.getStatus()).isEqualTo(TodoListStatus.IN_PROGRESS);

        verify(this.todoListRepository).save(any(TodoList.class));
    }

    @Test
    public void update_missingTodoList_throwsException() {

        TodoList updatedList = this.todoList;
        updatedList.setStatus(TodoListStatus.IN_PROGRESS);

        when(this.todoListRepository.findById(updatedList.getId()))
                .thenReturn(Optional.empty());

        assertThrows(ListNotFoundException.class, () -> {
            this.todoListService.update(this.listUUID, this.updateRequest);
        });
    }

    @Test
    void delete_missingTodoList_throwsException() {

        doThrow(new IllegalArgumentException()).when(this.todoListRepository).deleteById(this.listUUID);
        assertThrows(ListNotFoundException.class, () -> {
            this.todoListService.delete(this.listUUID);
        });
    }


    @Test
    void list_withFilters_returnsTodoListPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<TodoList> page = new PageImpl<>(List.of(this.todoList), pageable, 1);

        when(todoListRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        Page<TodoList> result = todoListService.list(TodoListStatus.NOT_STARTED, null, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isSameAs(this.todoList);
    }




}



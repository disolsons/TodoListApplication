package org.jordi.solsona.todolistapplication.service;

import org.jordi.solsona.todolistapplication.commons.exceptions.ListNotFoundException;
import org.jordi.solsona.todolistapplication.domain.model.TodoList;
import org.jordi.solsona.todolistapplication.domain.model.TodoListStatus;
import org.jordi.solsona.todolistapplication.domain.repository.TodoListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TodoListServiceTest {

    @Mock
    private TodoListRepository todoListRepository;

    @InjectMocks
    private TodoListService todoListService;

    private TodoList todoList;
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

        when(this.todoListRepository.save(this.todoList)).thenReturn(this.todoList);
        TodoList createdList = this.todoListService.createTodoList(this.todoList);

        assertThat(createdList).isSameAs(this.todoList);
        verify(this.todoListRepository).save(this.todoList);
    }

    @Test
    public void update_existingTodoList_udpatesFieldsAndSavesList() {

        TodoList updated = new TodoList();
        updated.setName("updated name");
        updated.setDescription("updated description");
        updated.setDueDate(Instant.now());
        updated.setStatus(TodoListStatus.IN_PROGRESS);

        when(this.todoListRepository.findById(this.listUUID)).thenReturn(Optional.of(this.todoList));
        when(this.todoListRepository.save(this.todoList)).thenReturn(this.todoList);

        TodoList result = this.todoListService.update(this.listUUID, updated);

        assertThat(result.getName()).isEqualTo("updated name");
        assertThat(result.getDescription()).isEqualTo("updated description");
        assertThat(result.getDueDate()).isEqualTo(updated.getDueDate());
        assertThat(result.getStatus()).isEqualTo(TodoListStatus.IN_PROGRESS);

        verify(this.todoListRepository).save(this.todoList);
    }
}



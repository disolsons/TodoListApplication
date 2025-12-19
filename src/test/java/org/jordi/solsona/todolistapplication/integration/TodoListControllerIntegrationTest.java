package org.jordi.solsona.todolistapplication.integration;

import org.jordi.solsona.todolistapplication.TodoListApplication;
import org.jordi.solsona.todolistapplication.api.dto.TodoListResponse;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.jordi.solsona.todolistapplication.domain.model.TodoList;
import org.jordi.solsona.todolistapplication.domain.model.TodoListStatus;
import org.jordi.solsona.todolistapplication.service.TodoListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoListControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private TodoList todoList;

    @BeforeEach
    void setUp() {
        this.todoList = new TodoList(UUID.randomUUID(), "title", "description", TodoListStatus.NOT_STARTED, Instant.now(), Instant.now(), Instant.now());
    }

    @Test
    void createTodoList_shouldReturnTodoListResponse() throws Exception {

        String requestBody = "{\"name\":\"Test Todo List\", \"status\":\"NOT_STARTED\", \"dueDate\":\"1766138400\"}";

        // Make POST request
        ResponseEntity<TodoListResponse> response = this.restTemplate.exchange(
                "/api/lists",
                HttpMethod.POST,
                new HttpEntity<>(requestBody, this.getJsonHeaders()),
                TodoListResponse.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Todo List", response.getBody().name());
        assertEquals("NOT_STARTED", response.getBody().status().name());
    }

    private HttpHeaders getJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}

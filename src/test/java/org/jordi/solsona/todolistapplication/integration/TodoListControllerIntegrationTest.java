package org.jordi.solsona.todolistapplication.integration;

import org.jordi.solsona.todolistapplication.api.dto.TodoResponse;
import org.jordi.solsona.todolistapplication.domain.model.Todo;
import org.jordi.solsona.todolistapplication.domain.model.TodoStatus;
import org.jordi.solsona.todolistapplication.domain.repository.TodoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoListControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TodoRepository todoRepository;

    private Todo todo;

    @BeforeEach
    void setUp() {
        this.todo = new Todo(UUID.randomUUID(), "title", "description", TodoStatus.NOT_STARTED, Instant.now(), Instant.now(), Instant.now());
    }

    @AfterEach
    void tearDown() {
        todoRepository.deleteAll();
    }

    @Test
    void createTodo_shouldReturnTodoResponse() throws Exception {

        String requestBody = "{\"name\":\"Test Todo List\", \"status\":\"NOT_STARTED\", \"dueDate\":\"1766138400\"}";

        // Make POST request
        ResponseEntity<TodoResponse> response = this.restTemplate.exchange(
                "/api/lists",
                HttpMethod.POST,
                new HttpEntity<>(requestBody, this.getJsonHeaders()),
                TodoResponse.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Todo List", response.getBody().name());
        assertEquals("NOT_STARTED", response.getBody().status().name());
    }

    @Test
    void getTodoById_shouldReturnTodoList() {
        String requestBody = """
                {
                  "name": "My List",
                  "status": "NOT_STARTED",
                  "dueDate": "1766138400"
                }
                """;

        //Add a record in the database so we can retrieve it later and test the getListById method.
        ResponseEntity<TodoResponse> createResponse =
                this.restTemplate.exchange(
                        "/api/lists",
                        HttpMethod.POST,
                        new HttpEntity<>(requestBody, getJsonHeaders()),
                        TodoResponse.class
                );

        UUID id = createResponse.getBody().id();

        ResponseEntity<TodoResponse> getResponse =
                restTemplate.getForEntity(
                        "/api/lists/" + id,
                        TodoResponse.class
                );

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(id, getResponse.getBody().id());
    }

    @Test
    void listTodo_shouldReturnPageOfTodos() {
        String requestBody = """
            {
              "name": "My Todo List",
              "status": "NOT_STARTED"
            }
            """;

        this.restTemplate.exchange(
                "/api/lists",
                HttpMethod.POST,
                new HttpEntity<>(requestBody, this.getJsonHeaders()),
                Map.class
        );

        ResponseEntity<Map> response =
                restTemplate.getForEntity("/api/lists", Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        List<?> content = (List<?>) response.getBody().get("content");
        assertNotNull(content);
        assertFalse(content.isEmpty());

        Map<?, ?> firstItem = (Map<?, ?>) content.get(0);

        assertEquals("My Todo List", firstItem.get("name"));
        assertEquals("NOT_STARTED", firstItem.get("status"));
        assertTrue(response.getBody().containsKey("totalElements"));
        assertTrue(response.getBody().containsKey("number"));
        assertTrue(response.getBody().containsKey("size"));
    }

    @Test
    void updateTodo_shouldReturnUpdatedTodoList() {
        String createBody = """
                {
                  "name": "Old Name",
                  "status": "NOT_STARTED",
                  "dueDate": "1766138400"
                }
                """;

        UUID id = restTemplate.exchange(
                "/api/lists",
                HttpMethod.POST,
                new HttpEntity<>(createBody, getJsonHeaders()),
                TodoResponse.class
        ).getBody().id();

        String updateBody = """
                {
                  "name": "Updated Name",
                  "status": "COMPLETED"
                }
                """;

        ResponseEntity<TodoResponse> updateResponse =
                restTemplate.exchange(
                        "/api/lists/" + id,
                        HttpMethod.PUT,
                        new HttpEntity<>(updateBody, getJsonHeaders()),
                        TodoResponse.class
                );

        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertEquals("Updated Name", updateResponse.getBody().name());
        assertEquals("COMPLETED", updateResponse.getBody().status().name());
    }

    @Test
    void deleteTodo_shouldReturnNoContent() {
        String requestBody = """
                {
                  "name": "To Delete",
                  "status": "NOT_STARTED",
                  "dueDate": "1766138400"
                }
                """;

        UUID id = restTemplate.exchange(
                "/api/lists",
                HttpMethod.POST,
                new HttpEntity<>(requestBody, getJsonHeaders()),
                TodoResponse.class
        ).getBody().id();

        ResponseEntity<Void> deleteResponse =
                restTemplate.exchange(
                        "/api/lists/" + id,
                        HttpMethod.DELETE,
                        null,
                        Void.class
                );

        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        ResponseEntity<String> getResponse =
                restTemplate.getForEntity("/api/lists/" + id, String.class);

        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    private HttpHeaders getJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}

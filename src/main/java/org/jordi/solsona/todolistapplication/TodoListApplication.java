    package org.jordi.solsona.todolistapplication;
    
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
    import org.springframework.context.annotation.ComponentScan;
    
    @SpringBootApplication
    @ComponentScan("org.jordi.solsona.todolistapplication")
    @EnableJpaRepositories("org.jordi.solsona.todolistapplication.domain.repository")
    public class TodoListApplication {
    
        public static void main(String[] args) {
            SpringApplication.run(TodoListApplication.class, args);
        }
    
    }

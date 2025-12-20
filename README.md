Pre-requisites: 

The application requires a postgresql database available. The default values for the database are below:
```
    url: jdbc:postgresql://localhost:5432/todolist_db
    username: postgres
    password: postgres
```
However, these can be modified in the file `application.yaml` to match any other user, password, or db url.



How to run (with Maven CLI):

`mvn clean install`
`mvn spring-boot:run`
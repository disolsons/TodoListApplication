package org.jordi.solsona.todolistapplication.commons.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ListNotFoundException extends RuntimeException{

    public ListNotFoundException(UUID listId) {
        super("List not found with id " + listId.toString());
    }
}

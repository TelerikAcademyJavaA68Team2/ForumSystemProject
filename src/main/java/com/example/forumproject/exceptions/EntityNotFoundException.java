package com.example.forumproject.exceptions;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String type, String attribute, String value) {
        super(String.format("%s with %s %s not found.", type, attribute, value));
    }

    public EntityNotFoundException(String type, Long id) {
        super(String.format("%s with id %d not found.", type, id));
    }

    public EntityNotFoundException(String type, String attribute, Long id) {
        super(String.format("There are no %s for %s with id %d", type, attribute, id));
    }

    public EntityNotFoundException(String type, Long userId, Long id) {
        super(String.format("There are no %s from user with %d for post with id %d", type, userId, id));
    }

    public EntityNotFoundException(String msg) {
        super(msg);
    }
}

package com.example.forumproject.exceptions;

public class DuplicateEntityException extends RuntimeException {
    public DuplicateEntityException(String type, String attribute, String value) {
        super(String.format("%s with %s %s already exists.", type, attribute, value));
    }

    public DuplicateEntityException(String type, Long id) {
        super(String.format("%s with id %d already exists.", type, id));
    }

    public DuplicateEntityException(String msg) {
        super(msg);
    }
}

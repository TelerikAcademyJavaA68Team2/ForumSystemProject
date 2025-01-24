package com.example.forumproject.exceptions;

public class InvalidEmailFormatException extends RuntimeException {
    public InvalidEmailFormatException(String email) {
        super(String.format("The email address: %s is invalid.", email));
    }
}

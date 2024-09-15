package com.example.freeandnice.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email) {
        super("User with not found by email " + email);
    }
}

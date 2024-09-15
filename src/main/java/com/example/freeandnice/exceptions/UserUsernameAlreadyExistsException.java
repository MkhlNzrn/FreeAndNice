package com.example.freeandnice.exceptions;

public class UserUsernameAlreadyExistsException extends RuntimeException {
    public UserUsernameAlreadyExistsException(String username) {
        super("User with this username already exists " + username);
    }
}

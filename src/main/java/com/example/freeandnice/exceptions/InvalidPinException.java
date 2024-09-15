package com.example.freeandnice.exceptions;

public class InvalidPinException extends RuntimeException {
    public InvalidPinException(Long pin) {
        super("Invalid pin: " + pin);
    }
}

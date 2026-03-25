package com.blackjack.shared.domain.exception;

public class InvalidNameException extends RuntimeException {
    public InvalidNameException(String message) {
        super(message);
    }
}

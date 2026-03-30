package com.blackjack.game.domain.model.exception;

public class InvalidMoveException extends RuntimeException {
    
    public InvalidMoveException(String action) {
        super(String.format("Unknown action: %s", action));
    }
}


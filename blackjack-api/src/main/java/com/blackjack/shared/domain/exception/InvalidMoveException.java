package com.blackjack.shared.domain.exception;

public class InvalidMoveException extends RuntimeException {
    
    public InvalidMoveException(String message) {
        super(message);
    }
    
    public InvalidMoveException(String gameId, String reason, Throwable cause) {
        super("Invalid move for game [" + gameId + "]: " + reason, cause);
    }
}


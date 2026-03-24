package com.blackjack.shared.domain.exception;

public class GameNotFoundException extends RuntimeException {
    
    public GameNotFoundException(String gameId) {
        super("Game with id [" + gameId + "] not found");
    }
    
    public GameNotFoundException(String gameId, Throwable cause) {
        super("Game with id [" + gameId + "] not found", cause);
    }
}



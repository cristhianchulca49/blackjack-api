package com.blackjack.shared.domain.exception;

public class PlayerNotFoundException extends RuntimeException {
    
    public PlayerNotFoundException(String playerId) {
        super("Player with id [" + playerId + "] not found");
    }
    
    public PlayerNotFoundException(String playerId, Throwable cause) {
        super("Player with id [" + playerId + "] not found", cause);
    }
}



package com.blackjack.shared.domain.exception;

public class GameAlreadyFinishedException extends RuntimeException {
    
    public GameAlreadyFinishedException(String gameId) {
        super("Game with id [" + gameId + "] is already finished");
    }
}


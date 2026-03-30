package com.blackjack.game.domain.model.exception;

public class GameAlreadyFinishedException extends DomainException {


    public GameAlreadyFinishedException() {
        super("Game is already finished");
    }
}


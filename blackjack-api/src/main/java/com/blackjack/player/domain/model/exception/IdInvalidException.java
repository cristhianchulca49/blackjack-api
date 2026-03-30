package com.blackjack.player.domain.model.exception;

import com.blackjack.game.domain.model.exception.DomainException;

public class IdInvalidException extends DomainException {
    public IdInvalidException(){
        super("Id cannot be null or blank");
    }
}

package com.blackjack.game.domain.model.exception;

public class IdInvalidException extends DomainException {
    public IdInvalidException(){
        super("Id cannot be null or blank");
    }
}

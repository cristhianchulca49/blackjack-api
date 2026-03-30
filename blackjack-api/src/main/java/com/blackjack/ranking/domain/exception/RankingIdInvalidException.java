package com.blackjack.ranking.domain.exception;

public class RankingIdInvalidException extends RuntimeException {
    public RankingIdInvalidException() {
        super("Ranking ID cannot be null or blank");
    }
}


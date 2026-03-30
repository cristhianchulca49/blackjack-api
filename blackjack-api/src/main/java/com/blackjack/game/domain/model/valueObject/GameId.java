package com.blackjack.game.domain.model.valueObject;

import com.blackjack.game.domain.model.exception.IdInvalidException;

import java.util.UUID;

public class GameId {
    private final String value;

    private GameId(String value) {
        if (value == null || value.isBlank()) {
            throw new IdInvalidException();
        }
        this.value = value;
    }

    public static GameId generate() {
        return new GameId(UUID.randomUUID().toString());
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameId gameId = (GameId) o;
        return value.equals(gameId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}


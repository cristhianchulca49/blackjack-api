package com.blackjack.player.domain.model;

import com.blackjack.game.domain.model.exception.IdInvalidException;

import java.util.UUID;

public class PlayerId {
    private final String value;

    private PlayerId(String value) {
        if (value == null || value.isBlank()) {
            throw new IdInvalidException();
        }
        this.value = value;
    }

    public static PlayerId generate() {
        return new PlayerId(UUID.randomUUID().toString());
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
        PlayerId gameId = (PlayerId) o;
        return value.equals(gameId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}


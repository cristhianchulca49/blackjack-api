package com.blackjack.ranking.domain.model.valueObject;

import com.blackjack.ranking.domain.exception.InvalidPositionException;

public class Position {
    private final int value;

    private Position(int value) {
        if (value <= 0) {
            throw new InvalidPositionException("Position must be greater than 0");
        }
        this.value = value;
    }

    public static Position of(int value) {
        return new Position(value);
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return value == position.value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}


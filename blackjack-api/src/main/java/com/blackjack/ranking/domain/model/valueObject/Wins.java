package com.blackjack.ranking.domain.model.valueObject;

import com.blackjack.ranking.domain.exception.InvalidWinsException;

public class Wins {
    private final int value;

    private Wins(int value) {
        if (value < 0) {
            throw new InvalidWinsException("Wins cannot be negative");
        }
        this.value = value;
    }

    public static Wins of(int value) {
        return new Wins(value);
    }

    public static Wins zero() {
        return new Wins(0);
    }

    public Wins increment() {
        return new Wins(this.value + 1);
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wins wins = (Wins) o;
        return value == wins.value;
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


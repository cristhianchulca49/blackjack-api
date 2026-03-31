package com.blackjack.ranking.domain.model.valueObject;

import com.blackjack.ranking.domain.exception.InvalidPositionException;

public class Draws {
    private final int value;

    private Draws(int value) {
        if (value < 0) {
            throw new InvalidPositionException("Draws cannot be negative");
        }
        this.value = value;
    }

    public static Draws of(int value) {
        return new Draws(value);
    }

    public static Draws zero() {
        return new Draws(0);
    }

    public Draws increment() {
        return new Draws(this.value + 1);
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Draws draws = (Draws) o;
        return value == draws.value;
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


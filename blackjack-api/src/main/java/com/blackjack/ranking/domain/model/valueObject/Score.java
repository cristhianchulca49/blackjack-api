package com.blackjack.ranking.domain.model.valueObject;

import com.blackjack.ranking.domain.exception.InvalidPositionException;

public class Score {
    private final int value;

    private Score(int value) {
        if (value < 0) {
            throw new InvalidPositionException("Score cannot be negative");
        }
        this.value = value;
    }

    public static Score of(int value) {
        return new Score(value);
    }

    public static Score calculate(Wins wins, Draws draws) {
        int v = (wins.getValue() * 3) + (draws.getValue());
        return new Score(v);
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Score score = (Score) o;
        return value == score.value;
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


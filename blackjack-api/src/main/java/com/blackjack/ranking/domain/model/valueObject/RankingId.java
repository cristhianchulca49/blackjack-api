package com.blackjack.ranking.domain.model.valueObject;

import com.blackjack.ranking.domain.exception.RankingIdInvalidException;

import java.util.UUID;

public class RankingId {
    private final String value;

    private RankingId(String value) {
        if (value == null || value.isBlank()) {
            throw new RankingIdInvalidException();
        }
        this.value = value;
    }

    public static RankingId generate() {
        return new RankingId(UUID.randomUUID().toString());
    }

    public static RankingId reconstitute(String value) {
        return new RankingId(value);
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
        RankingId rankingId = (RankingId) o;
        return value.equals(rankingId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}


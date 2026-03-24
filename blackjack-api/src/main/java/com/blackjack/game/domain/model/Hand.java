package com.blackjack.game.domain.model;

import com.blackjack.game.domain.model.valueObject.Card;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private final List<Card> cards;

    private Hand(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
    }

    public static Hand of(Card card1, Card card2) {
        if (card1 == null || card2 == null) {
            throw new IllegalArgumentException("Cards cannot be null");
        }
        if(card1.equals(card2)) {
            throw new IllegalArgumentException("Cards cannot be the same");
        }
        return new Hand(List.of(card1, card2));
    }

    public Hand addCard(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }
        if (cards.contains(card)) {
            throw new IllegalArgumentException("Card already exists");
        }
        List<Card> newCards = new ArrayList<>(this.cards);
        newCards.add(card);
        return new Hand(newCards);
    }

    public int score() {
        int total = 0;
        int aces = 0;

        for (Card card : cards) {
            total += card.value();
            if (card.rank() == Card.Rank.ACE) aces++;
        }

        while (total > 21 && aces > 0) {
            total -= 10;
            aces--;
        }

        return total;
    }

    public boolean isBust() {
        return score() > 21;
    }

    public boolean isBlackjack() {
        return cards.size() == 2 && score() == 21;
    }
}

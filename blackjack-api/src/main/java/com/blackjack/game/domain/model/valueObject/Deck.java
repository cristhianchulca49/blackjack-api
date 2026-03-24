package com.blackjack.game.domain.model.valueObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards;

    private Deck(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
    }

    public static Deck fullShuffled() {
        List<Card> cards = new ArrayList<>();
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
        Collections.shuffle(cards);
        return new Deck(cards);
    }

    public DealResult deal(int count) {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Deck is empty");
        }
        if (count > cards.size()) {
            throw new IllegalArgumentException("Not enough cards in deck: requested " + count + ", available " + cards.size());
        }
        List<Card> dealtCards = cards.subList(0, count);
        List<Card> remaining = cards.subList(count, cards.size());
        return new DealResult(dealtCards, new Deck(remaining));
    }

    public record DealResult(List<Card> cards, Deck remainingDeck) {
    }
}

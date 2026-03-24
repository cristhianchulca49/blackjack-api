package com.blackjack.game.domain.model;

import com.blackjack.game.domain.model.valueObject.Deck;
import com.blackjack.game.domain.model.valueObject.GameStatus;

import java.util.List;
import java.util.UUID;

public class Game {
    private String id;
    private String playerId;
    private Hand dealerHand;
    private Hand playerHand;
    private Deck deck;
    private GameStatus status;

    public Game(String id, String playerId, Hand dealerHand, Hand playerHand, Deck deck, GameStatus status) {
        this.id = id;
        this.playerId = playerId;
        this.dealerHand = dealerHand;
        this.playerHand = playerHand;
        this.deck = deck;
        this.status = status;
    }

    public static Game create(String playerId) {
        String id = UUID.randomUUID().toString();
        Deck deck = Deck.fullShuffled();
        Hand dealerHand = Hand.of(List.of(deck.deal(), deck.deal()));
        Hand playerHand = Hand.of(List.of(deck.deal(), deck.deal()));
        return new Game(id, playerId, dealerHand, playerHand, deck, GameStatus.IN_PROGRESS);
    }

    public String getId() {
        return id;
    }

    public String getPlayerId() {
        return playerId;
    }

    public Hand getDealerHand() {
        return dealerHand;
    }

    public Hand getPlayerHand() {
        return playerHand;
    }

    public Deck getDeck() {
        return deck;
    }

    public GameStatus getStatus() {
        return status;
    }
}
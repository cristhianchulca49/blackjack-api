package com.blackjack.game.domain.model;

import com.blackjack.game.domain.model.valueObject.Deck;
import com.blackjack.game.domain.model.valueObject.GameStatus;

import java.util.UUID;

import static com.blackjack.game.domain.model.valueObject.GameStatus.*;

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

    public static Game create(String playerId, Hand playerHand,
                              Hand dealerHand, Deck deck) {
        return new Game(
                UUID.randomUUID().toString(),
                playerId,
                playerHand,
                dealerHand,
                deck,
                GameStatus.IN_PROGRESS);
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
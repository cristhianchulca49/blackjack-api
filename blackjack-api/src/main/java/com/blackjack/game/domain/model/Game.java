package com.blackjack.game.domain.model;

import com.blackjack.game.domain.model.valueObject.Deck;
import com.blackjack.game.domain.model.valueObject.GameId;
import com.blackjack.game.domain.model.valueObject.GameStatus;
import com.blackjack.player.domain.model.PlayerId;

import java.util.UUID;

import static com.blackjack.game.domain.model.valueObject.GameStatus.*;

public class Game {
    private GameId id;
    private PlayerId playerId;
    private Hand dealerHand;
    private Hand playerHand;
    private Deck deck;
    private GameStatus status;

    private Game(GameId id, PlayerId playerId, Hand dealerHand, Hand playerHand, Deck deck, GameStatus status) {
        this.id = id;
        this.playerId = playerId;
        this.dealerHand = dealerHand;
        this.playerHand = playerHand;
        this.deck = deck;
        this.status = status;
    }

    public static Game create(GameId id, PlayerId playerId, Hand dealerHand, Hand playerHand, Deck deck, GameStatus status) {
        if (id == null) id = GameId.generate();
        return new Game(id, playerId, dealerHand, playerHand, deck, status);
    }

    public GameId getId() {
        return id;
    }

    public PlayerId getPlayerId() {
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
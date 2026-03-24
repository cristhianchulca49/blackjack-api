package com.blackjack.game.domain.model.builder;

import com.blackjack.game.domain.model.Game;
import com.blackjack.game.domain.model.Hand;
import com.blackjack.game.domain.model.valueObject.Deck;
import com.blackjack.game.domain.model.valueObject.GameStatus;

public class GameBuilder {
    private final String id;
    private final String playerId;

    private Hand dealerHand;
    private Hand playerHand;
    private Deck deck;
    private GameStatus status;

    public GameBuilder(Game currentGame) {
        this.id = currentGame.getId();
        this.playerId = currentGame.getPlayerId();
        this.dealerHand = currentGame.getDealerHand();
        this.playerHand = currentGame.getPlayerHand();
        this.deck = currentGame.getDeck();
        this.status = currentGame.getStatus();
    }

    public GameBuilder withPlayerHand(Hand playerHand) {
        this.playerHand = playerHand;
        return this;
    }

    public GameBuilder withDealerHand(Hand dealerHand) {
        this.dealerHand = dealerHand;
        return this;
    }

    public GameBuilder withDeck(Deck deck) {
        this.deck = deck;
        return this;
    }

    public GameBuilder withStatus(GameStatus status) {
        this.status = status;
        return this;
    }

    public Game build() {
        return new Game(id, playerId, dealerHand, playerHand, deck, status);
    }
}
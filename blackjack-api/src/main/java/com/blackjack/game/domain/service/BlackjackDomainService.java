package com.blackjack.game.domain.service;

import com.blackjack.game.domain.model.Game;
import com.blackjack.game.domain.model.Hand;
import com.blackjack.game.domain.model.builder.GameBuilder;
import com.blackjack.game.domain.model.exception.GameAlreadyFinishedException;
import com.blackjack.game.domain.model.valueObject.Card;
import com.blackjack.game.domain.model.valueObject.Deck;
import com.blackjack.game.domain.model.valueObject.GameStatus;
import com.blackjack.player.domain.model.PlayerId;

import java.util.List;

import static com.blackjack.game.domain.model.valueObject.GameStatus.*;

public class BlackjackDomainService {

    public Game dealInitialCards(PlayerId playerId) {
        Deck.DealResult deckAndCards = Deck.fullShuffled().deal(4);
        List<Card> cards = deckAndCards.cards();

        Hand dealerHand = Hand.of(cards.get(0), cards.get(2));
        Hand playerHand = Hand.of(cards.get(1), cards.get(3));

        return new GameBuilder(playerId)
                .withDeck(deckAndCards.remainingDeck())
                .withDealerHand(dealerHand)
                .withPlayerHand(playerHand)
                .withStatus(from(dealerHand, playerHand))
                .build();
    }

    public Game hit(Game game) {
        if (game.getStatus() != IN_PROGRESS) {
            throw new GameAlreadyFinishedException();
        }
        Deck.DealResult deckAndCards = game.getDeck().deal(1);
        Hand playerHand = game.getPlayerHand().addCard(deckAndCards.cards().getFirst());

        return new GameBuilder(game)
                .withDeck(deckAndCards.remainingDeck())
                .withPlayerHand(playerHand)
                .withStatus(from(game.getDealerHand(), playerHand))
                .build();
    }

    public Game stand(Game game) {
        if (game.getStatus() != IN_PROGRESS) {
            throw new GameAlreadyFinishedException();
        }
        return resolveDealerTurn(game);
    }

    private Game resolveDealerTurn(Game game) {
        Deck deck = game.getDeck();
        Hand dealerHand = game.getDealerHand();

        while (dealerHand.score() < 17) {
            Deck.DealResult deckAndCards = deck.deal(1);
            dealerHand = dealerHand.addCard(deckAndCards.cards().getFirst());
            deck = deckAndCards.remainingDeck();
        }

        return new GameBuilder(game)
                .withDeck(deck)
                .withDealerHand(dealerHand)
                .withStatus(resolveWinner(dealerHand, game.getPlayerHand()))
                .build();
    }

    private GameStatus resolveWinner(Hand dealerHand, Hand playerHand) {
        if (playerHand.isBust()) return DEALER_WINS;
        if (dealerHand.isBust()) return PLAYER_WINS;
        if (playerHand.score() > dealerHand.score()) return PLAYER_WINS;
        if (dealerHand.score() > playerHand.score()) return DEALER_WINS;
        return DRAW;
    }

    private GameStatus from(Hand dealer, Hand player) {
        if (player.isBlackjack() && dealer.isBlackjack()) return DRAW;
        if (player.isBlackjack()) return PLAYER_WINS;
        if (dealer.isBlackjack()) return DEALER_WINS;
        if (player.isBust()) return DEALER_WINS;
        return IN_PROGRESS;
    }
}
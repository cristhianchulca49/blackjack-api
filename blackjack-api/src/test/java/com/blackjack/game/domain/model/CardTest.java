package com.blackjack.game.domain.model;

import com.blackjack.game.domain.model.valueObject.Card;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Card Value Object Tests")
class CardTest {
    
    @Test
    @DisplayName("should create card with suit and rank")
    void shouldCreateCard() {
        Card card = new Card(Card.Suit.HEARTS, Card.Rank.KING);
        
        assertThat(card.suit()).isEqualTo(Card.Suit.HEARTS);
        assertThat(card.rank()).isEqualTo(Card.Rank.KING);
    }
    
    @Test
    @DisplayName("should return correct value for number cards")
    void shouldReturnValueForNumberCards() {
        assertThat(new Card(Card.Suit.HEARTS, Card.Rank.TWO).value()).isEqualTo(2);
        assertThat(new Card(Card.Suit.HEARTS, Card.Rank.FIVE).value()).isEqualTo(5);
        assertThat(new Card(Card.Suit.HEARTS, Card.Rank.TEN).value()).isEqualTo(10);
    }
    
    @Test
    @DisplayName("should return value 10 for face cards")
    void shouldReturnValue10ForFaceCards() {
        assertThat(new Card(Card.Suit.DIAMONDS, Card.Rank.JACK).value()).isEqualTo(10);
        assertThat(new Card(Card.Suit.CLUBS, Card.Rank.QUEEN).value()).isEqualTo(10);
        assertThat(new Card(Card.Suit.SPADES, Card.Rank.KING).value()).isEqualTo(10);
    }
    
    @Test
    @DisplayName("should return value 11 for ACE")
    void shouldReturnValue11ForAce() {
        Card ace = new Card(Card.Suit.HEARTS, Card.Rank.ACE);
        assertThat(ace.value()).isEqualTo(11);
    }
    
    @Test
    @DisplayName("should create cards of all suits")
    void shouldCreateCardsOfAllSuits() {
        for (Card.Suit suit : Card.Suit.values()) {
            Card card = new Card(suit, Card.Rank.KING);
            assertThat(card.suit()).isEqualTo(suit);
        }
    }
    
    @Test
    @DisplayName("should create cards of all ranks")
    void shouldCreateCardsOfAllRanks() {
        for (Card.Rank rank : Card.Rank.values()) {
            Card card = new Card(Card.Suit.HEARTS, rank);
            assertThat(card.rank()).isEqualTo(rank);
        }
    }
    
    @Test
    @DisplayName("should have 52 possible cards (4 suits × 13 ranks)")
    void shouldHave52PossibleCards() {
        int totalCards = Card.Suit.values().length * Card.Rank.values().length;
        assertThat(totalCards).isEqualTo(52);
    }
}


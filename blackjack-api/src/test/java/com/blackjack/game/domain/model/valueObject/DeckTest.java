package com.blackjack.game.domain.model.valueObject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Deck Value Object Tests")
class DeckTest {
    
    @Test
    @DisplayName("should create a full deck with 52 cards")
    void shouldCreateFullDeck() {
        Deck deck = Deck.fullShuffled();
        
        Deck.DealResult result = deck.deal(52);
        assertThat(result.cards()).hasSize(52);
        
        Deck empty = result.remainingDeck();
        assertThatThrownBy(() -> empty.deal(1))
            .isInstanceOf(IllegalStateException.class);
    }
    
    @Test
    @DisplayName("should contain all 52 unique cards")
    void shouldContainAll52UniqueCards() {
        Deck deck = Deck.fullShuffled();
        Deck.DealResult result = deck.deal(52);
        
        Set<Card> cards = new HashSet<>(result.cards());
        assertThat(cards).hasSize(52);
    }
    
    @Test
    @DisplayName("should shuffle cards (order should be randomized)")
    void shouldShuffleCards() {
        Deck deck1 = Deck.fullShuffled();
        Deck deck2 = Deck.fullShuffled();
        
        Deck.DealResult result1 = deck1.deal(52);
        Deck.DealResult result2 = deck2.deal(52);
        
        assertThat(result1.cards()).isNotEqualTo(result2.cards());
    }
    
    @Test
    @DisplayName("should deal exact number of cards")
    void shouldDealExactNumberOfCards() {
        Deck deck = Deck.fullShuffled();
        
        Deck.DealResult result = deck.deal(4);
        assertThat(result.cards()).hasSize(4);
        assertThat(result.remainingDeck().deal(48).cards()).hasSize(48);
    }
    
    @Test
    @DisplayName("should return remaining deck after dealing")
    void shouldReturnRemainingDeckAfterDealing() {
        Deck deck = Deck.fullShuffled();
        
        Deck.DealResult result = deck.deal(10);
        int remaining = result.remainingDeck().deal(42).cards().size();
        
        assertThat(remaining).isEqualTo(42);
    }
    
    @Test
    @DisplayName("should throw when dealing from empty deck")
    void shouldThrowWhenDealingFromEmptyDeck() {
        Deck deck = Deck.fullShuffled();
        
        Deck.DealResult result = deck.deal(52);
        Deck emptyDeck = result.remainingDeck();
        
        assertThatThrownBy(() -> emptyDeck.deal(1))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Deck is empty");
    }
    
    @Test
    @DisplayName("should throw when requesting more cards than available")
    void shouldThrowWhenNotEnoughCardsAvailable() {
        Deck deck = Deck.fullShuffled();
        
        assertThatThrownBy(() -> deck.deal(100))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Not enough cards")
            .hasMessageContaining("requested 100")
            .hasMessageContaining("available 52");
    }
    
    @Test
    @DisplayName("should throw with accurate counts when not enough cards in remaining deck")
    void shouldThrowWithAccurateCounts() {
        Deck deck = Deck.fullShuffled();
        
        Deck.DealResult result = deck.deal(50);
        Deck remaining = result.remainingDeck();
        
        assertThatThrownBy(() -> remaining.deal(5))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("requested 5")
            .hasMessageContaining("available 2");
    }
    
    @Test
    @DisplayName("should deal 1 card successfully")
    void shouldDeal1Card() {
        Deck deck = Deck.fullShuffled();
        
        Deck.DealResult result = deck.deal(1);
        assertThat(result.cards()).hasSize(1);
    }
    
    @Test
    @DisplayName("should deal 2 cards for initial game setup")
    void shouldDeal2CardsForInitialSetup() {
        Deck deck = Deck.fullShuffled();
        
        Deck.DealResult result = deck.deal(4);  // 2 for player, 2 for dealer
        assertThat(result.cards()).hasSize(4);
    }
    
    @Test
    @DisplayName("should maintain immutability: dealing does not modify original deck")
    void shouldMaintainImmutability() {
        Deck original = Deck.fullShuffled();
        Deck.DealResult result = original.deal(10);

        assertThat(result.remainingDeck()).isNotSameAs(original);
    }
}



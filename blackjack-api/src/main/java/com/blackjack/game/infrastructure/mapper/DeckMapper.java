package com.blackjack.game.infrastructure.mapper;

import com.blackjack.game.domain.model.valueObject.Deck;
import com.blackjack.game.infrastructure.adapter.out.persistence.document.DeckDocument;

public class DeckMapper {
    public static DeckDocument toDocument(Deck deck) {
        return new DeckDocument(deck.getCards().stream()
                .map(CardMapper::toDocument)
                .toList());
    }

    public static Deck toDomain(DeckDocument deckDocument) {
        return Deck.reconstitute(deckDocument.cards().stream()
                .map(CardMapper::toDomain)
                .toList());
    }
}

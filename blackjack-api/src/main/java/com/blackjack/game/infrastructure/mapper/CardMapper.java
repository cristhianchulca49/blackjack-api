package com.blackjack.game.infrastructure.mapper;

import com.blackjack.game.domain.model.valueObject.Card;
import com.blackjack.game.infrastructure.adapter.in.rest.dto.response.CardResponse;
import com.blackjack.game.infrastructure.adapter.out.persistence.document.CardDocument;

public class CardMapper {
    public static CardDocument toDocument(Card card) {
        return new CardDocument(card.rank().name(), card.suit().name());
    }

    public static Card toDomain(CardDocument card) {
        return new Card(
                Card.Suit.valueOf(card.suit()),
                Card.Rank.valueOf(card.rank())
        );
    }

    public static CardResponse toResponse(Card card) {
        return new CardResponse(
                card.rank().name(),
                card.suit().name()
        );
    }
}

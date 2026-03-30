package com.blackjack.game.infrastructure.mapper;

import com.blackjack.game.domain.model.Hand;
import com.blackjack.game.infrastructure.adapter.out.persistence.document.HandDocument;

public class HandMapper {

    public static HandDocument toDocument(Hand hand) {
        return new HandDocument(hand.getCards().stream()
                .map(CardMapper::toDocument)
                .toList());
    }

    public static Hand toDomain(HandDocument handDocument) {
        return Hand.reconstitute(handDocument.cards().stream()
                .map(CardMapper::toDomain)
                .toList());
    }
}

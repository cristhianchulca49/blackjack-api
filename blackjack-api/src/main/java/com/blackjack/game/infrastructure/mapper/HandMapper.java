package com.blackjack.game.infrastructure.mapper;

import com.blackjack.game.domain.model.Hand;
import com.blackjack.game.infrastructure.adapter.in.rest.dto.response.CardResponse;
import com.blackjack.game.infrastructure.adapter.in.rest.dto.response.HandResponse;
import com.blackjack.game.infrastructure.adapter.out.persistence.document.HandDocument;

import java.util.ArrayList;
import java.util.List;

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

    public static HandResponse toResponse(Hand hand, boolean hideSecondCard) {
        var cards = hand.getCards();
        var responses = new ArrayList<CardResponse>();
        for (int i = 0; i < cards.size(); i++) {
            if (hideSecondCard && i == 1) {
                responses.add(new CardResponse("HIDDEN","HIDDEN"));
            } else {
                responses.add(CardMapper.toResponse(cards.get(i)));
            }
        }
        return new HandResponse(List.copyOf(responses));
    }
}

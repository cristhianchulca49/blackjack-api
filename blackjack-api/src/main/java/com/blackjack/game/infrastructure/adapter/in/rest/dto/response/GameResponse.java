package com.blackjack.game.infrastructure.adapter.in.rest.dto.response;

public record GameResponse(
        String id,
        String playerId,
        HandResponse playerHand,
        HandResponse dealerHand,
        DeckResponse deck,
        String status
) {
}

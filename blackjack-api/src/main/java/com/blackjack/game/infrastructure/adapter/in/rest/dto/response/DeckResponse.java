package com.blackjack.game.infrastructure.adapter.in.rest.dto.response;

import java.util.List;

public record DeckResponse(
        List<CardResponse> cards
) {
}


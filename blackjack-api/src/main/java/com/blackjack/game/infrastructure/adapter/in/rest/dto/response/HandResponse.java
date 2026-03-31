package com.blackjack.game.infrastructure.adapter.in.rest.dto.response;

import java.util.List;

public record HandResponse(
        List<CardResponse> cards
) {
}


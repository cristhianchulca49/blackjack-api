package com.blackjack.ranking.infrastructure.adapter.in.rest.dto.response;

import lombok.Builder;

@Builder
public record RankingResponse(
        int position,
        String username,
        int totalWins,
        int totalDraws,
        int score) {

}


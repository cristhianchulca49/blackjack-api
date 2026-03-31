package com.blackjack.ranking.domain.port.in;

import com.blackjack.player.domain.model.Name;
import com.blackjack.player.domain.model.PlayerId;
import com.blackjack.ranking.domain.model.Ranking;
import reactor.core.publisher.Mono;

public interface CreateRankingUseCase {
    Mono<Ranking> execute(PlayerId playerId, Name name);
}


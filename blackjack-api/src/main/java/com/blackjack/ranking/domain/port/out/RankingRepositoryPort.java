package com.blackjack.ranking.domain.port.out;

import com.blackjack.player.domain.model.PlayerId;
import com.blackjack.ranking.domain.model.Ranking;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RankingRepositoryPort {
    Mono<Ranking> save(Ranking ranking);

    Flux<Ranking> findTopByLimit(int limit);

    Mono<Ranking> findByPlayerId(PlayerId playerId);
}


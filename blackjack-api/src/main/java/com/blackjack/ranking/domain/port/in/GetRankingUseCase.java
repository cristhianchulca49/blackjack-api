package com.blackjack.ranking.domain.port.in;

import com.blackjack.ranking.domain.model.Ranking;
import reactor.core.publisher.Flux;

public interface GetRankingUseCase {
    Flux<Ranking> execute(int limit);
}


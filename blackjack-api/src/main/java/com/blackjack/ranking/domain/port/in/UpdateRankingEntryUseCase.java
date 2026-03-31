package com.blackjack.ranking.domain.port.in;

import com.blackjack.game.domain.model.valueObject.GameStatus;
import com.blackjack.player.domain.model.PlayerId;
import com.blackjack.ranking.domain.model.Ranking;
import reactor.core.publisher.Mono;

public interface UpdateRankingEntryUseCase {
    Mono<Ranking> execute(PlayerId playerId, GameStatus status);
}


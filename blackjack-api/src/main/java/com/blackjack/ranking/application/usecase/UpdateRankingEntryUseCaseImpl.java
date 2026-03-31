package com.blackjack.ranking.application.usecase;

import com.blackjack.game.domain.model.valueObject.GameStatus;
import com.blackjack.player.domain.model.PlayerId;
import com.blackjack.ranking.domain.model.Ranking;
import com.blackjack.ranking.domain.port.in.UpdateRankingEntryUseCase;
import com.blackjack.ranking.domain.port.out.RankingRepositoryPort;
import com.blackjack.shared.application.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UpdateRankingEntryUseCaseImpl implements UpdateRankingEntryUseCase {

    private final RankingRepositoryPort rankingRepositoryPort;

    public UpdateRankingEntryUseCaseImpl(RankingRepositoryPort rankingRepositoryPort) {
        this.rankingRepositoryPort = rankingRepositoryPort;
    }

    @Override
    public Mono<Ranking> execute(PlayerId playerId, GameStatus status) {
        return rankingRepositoryPort.findByPlayerId(playerId)
                .switchIfEmpty(Mono.error(() -> new ResourceNotFoundException("Ranking", playerId.getValue())))
                .map(ranking -> ranking.recordResult(status))
                .flatMap(rankingRepositoryPort::save);
    }
}



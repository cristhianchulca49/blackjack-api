package com.blackjack.ranking.application.usecase;

import com.blackjack.player.domain.model.Name;
import com.blackjack.player.domain.model.PlayerId;
import com.blackjack.ranking.domain.model.Ranking;
import com.blackjack.ranking.domain.port.in.CreateRankingUseCase;
import com.blackjack.ranking.domain.port.out.RankingRepositoryPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CreateRankingUseCaseImpl implements CreateRankingUseCase {

    private final RankingRepositoryPort rankingRepositoryPort;

    public CreateRankingUseCaseImpl(RankingRepositoryPort rankingRepositoryPort) {
        this.rankingRepositoryPort = rankingRepositoryPort;
    }

    @Override
    public Mono<Ranking> execute(PlayerId playerId, Name name) {
        return Mono.fromCallable(() -> Ranking.create(playerId, name))
                .flatMap(rankingRepositoryPort::save);
    }
}

package com.blackjack.ranking.application.usecase;

import com.blackjack.ranking.domain.model.Ranking;
import com.blackjack.ranking.domain.port.in.GetRankingUseCase;
import com.blackjack.ranking.domain.port.out.RankingRepositoryPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class GetRankingUseCaseImpl implements GetRankingUseCase {

    private final RankingRepositoryPort rankingRepositoryPort;

    public GetRankingUseCaseImpl(RankingRepositoryPort rankingRepositoryPort) {
        this.rankingRepositoryPort = rankingRepositoryPort;
    }

    @Override
    public Flux<Ranking> execute(int limit) {
        return rankingRepositoryPort.findTopByLimit(limit);
    }
}


package com.blackjack.ranking.infrastructure.adapter.out.persistence;

import com.blackjack.game.domain.model.exception.DomainException;
import com.blackjack.player.domain.model.PlayerId;
import com.blackjack.ranking.domain.model.Ranking;
import com.blackjack.ranking.domain.port.out.RankingRepositoryPort;
import com.blackjack.ranking.infrastructure.adapter.out.persistence.entity.RankingEntity;
import com.blackjack.ranking.infrastructure.adapter.out.persistence.repository.RankingR2bdcRepository;
import com.blackjack.ranking.infrastructure.mapper.RankingMapper;
import com.blackjack.shared.infrastructure.exception.DatabaseException;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class RankingRepositoryAdapter implements RankingRepositoryPort {
    private final RankingR2bdcRepository rankingR2bdcRepository;

    public RankingRepositoryAdapter(RankingR2bdcRepository rankingR2bdcRepository) {
        this.rankingR2bdcRepository = rankingR2bdcRepository;
    }

    @Override
    public Mono<Ranking> save(Ranking ranking) {
        return rankingR2bdcRepository.save(RankingMapper.toEntity(ranking))
                .map(RankingMapper::toDomain)
                .onErrorMap(this::handleDatabaseError);
    }

    @Override
    public Flux<Ranking> findTopByLimit(int limit) {
        return rankingR2bdcRepository.findAllByOrderByWinsDesc(limit)
                .map(RankingMapper::toDomain)
                .take(limit)
                .onErrorMap(this::handleDatabaseError);
    }

    @Override
    public Mono<Ranking> findByPlayerId(PlayerId playerId) {
        return rankingR2bdcRepository.findByPlayerId(playerId.getValue())
                .map(RankingMapper::toDomain)
                .onErrorMap(this::handleDatabaseError);
    }

    private Throwable handleDatabaseError(Throwable ex) {
        if (ex instanceof DomainException) return ex;
        return new DatabaseException("Database failure while persisting ranking, error: " + ex.getMessage());
    }
}

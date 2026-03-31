package com.blackjack.ranking.infrastructure.adapter.out.persistence.repository;

import com.blackjack.ranking.infrastructure.adapter.out.persistence.entity.RankingEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RankingR2bdcRepository extends R2dbcRepository<RankingEntity, String> {
	@Query("SELECT * FROM ranking ORDER BY wins DESC LIMIT :limit")
	Flux<RankingEntity> findAllByOrderByWinsDesc(int limit);
	Mono<RankingEntity> findByPlayerId(String playerId);
}

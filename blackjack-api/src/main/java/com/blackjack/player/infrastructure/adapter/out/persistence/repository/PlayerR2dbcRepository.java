package com.blackjack.player.infrastructure.adapter.out.persistence.repository;

import com.blackjack.player.domain.model.Player;
import com.blackjack.player.infrastructure.adapter.out.persistence.entity.PlayerEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerR2dbcRepository extends R2dbcRepository<PlayerEntity, String>{
	Mono<Boolean> existsByName(String name);
}

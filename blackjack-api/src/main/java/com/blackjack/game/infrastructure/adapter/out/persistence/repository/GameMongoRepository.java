package com.blackjack.game.infrastructure.adapter.out.persistence.repository;

import com.blackjack.game.infrastructure.adapter.out.persistence.document.GameDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface GameMongoRepository extends ReactiveMongoRepository<GameDocument, String> {

    Flux<GameDocument> findByPlayerId(String value);
}
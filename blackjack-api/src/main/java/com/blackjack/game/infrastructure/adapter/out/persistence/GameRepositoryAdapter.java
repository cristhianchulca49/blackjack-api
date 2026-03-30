package com.blackjack.game.infrastructure.adapter.out.persistence;

import com.blackjack.game.domain.model.Game;
import com.blackjack.game.domain.model.exception.DomainException;
import com.blackjack.game.domain.model.valueObject.GameId;
import com.blackjack.game.domain.port.out.GameRepositoryPort;
import com.blackjack.game.infrastructure.adapter.out.persistence.repository.GameMongoRepository;
import com.blackjack.game.infrastructure.mapper.GameMapper;
import com.blackjack.player.domain.model.PlayerId;
import com.blackjack.shared.infrastructure.exception.DatabaseException;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class GameRepositoryAdapter implements GameRepositoryPort {
    private final GameMongoRepository gameMongoRepository;

    public GameRepositoryAdapter(GameMongoRepository gameMongoRepository) {
        this.gameMongoRepository = gameMongoRepository;
    }

    @Override
    public Mono<Game> save(Game game) {

        return gameMongoRepository.save(GameMapper.toDocument(game))
                .map(GameMapper::toDomain)
                .onErrorMap(this::handleDatabaseError);
    }

    @Override
    public Mono<Game> findById(GameId gameId) {
        return gameMongoRepository.findById(gameId.getValue())
                .map(GameMapper::toDomain)
                .onErrorMap(this::handleDatabaseError);
    }

    @Override
    public Flux<Game> findByPlayerId(PlayerId playerId) {
        return gameMongoRepository.findByPlayerId(playerId.getValue())
                .map(GameMapper::toDomain)
                .onErrorMap(this::handleDatabaseError);
    }

    @Override
    public Mono<Void> deleteById(GameId gameId) {
        return gameMongoRepository.deleteById(gameId.getValue())
                .onErrorMap(this::handleDatabaseError);
    }

    @Override
    public Mono<Boolean> existsById(GameId gameId) {
        return gameMongoRepository.existsById(gameId.getValue())
                .onErrorMap(this::handleDatabaseError);
    }

    private Throwable handleDatabaseError(Throwable ex) {
        if (ex instanceof DomainException) return ex;
        return new DatabaseException("Database failure while saving game, error: " + ex.getMessage());
    }
}

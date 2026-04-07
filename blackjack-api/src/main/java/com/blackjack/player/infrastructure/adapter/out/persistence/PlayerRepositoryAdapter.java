package com.blackjack.player.infrastructure.adapter.out.persistence;

import com.blackjack.game.domain.model.exception.DomainException;
import com.blackjack.player.domain.model.Player;
import com.blackjack.player.domain.model.PlayerId;
import com.blackjack.player.domain.port.out.PlayerRepositoryPort;
import com.blackjack.player.infrastructure.adapter.out.persistence.repository.PlayerR2dbcRepository;
import com.blackjack.player.infrastructure.mapper.PlayerMapper;
import com.blackjack.shared.infrastructure.exception.DatabaseException;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class PlayerRepositoryAdapter implements PlayerRepositoryPort {

    private final PlayerR2dbcRepository playerR2dbcRepository;

    public PlayerRepositoryAdapter(PlayerR2dbcRepository playerR2dbcRepository) {
        this.playerR2dbcRepository = playerR2dbcRepository;
    }

    @Override
    public Mono<Player> save(Player player) {
        return Mono.just(player)
                .map(PlayerMapper::toEntity)
                .flatMap(playerR2dbcRepository::save)
                .map(PlayerMapper::toDomain)
                .onErrorMap(this::handleDatabaseError);
    }

    @Override
    public Mono<Player> findById(PlayerId playerId) {
        return playerR2dbcRepository.findById(playerId.getValue())
                .map(PlayerMapper::toDomain)
                .onErrorMap(this::handleDatabaseError);
    }

    @Override
    public Mono<Void> deleteById(PlayerId playerId) {
        return playerR2dbcRepository.deleteById(playerId.getValue())
                .onErrorMap(this::handleDatabaseError);
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        return playerR2dbcRepository.existsByName(name)
                .onErrorMap(this::handleDatabaseError);
    }

    private Throwable handleDatabaseError(Throwable ex) {
        if (ex instanceof DomainException) return ex;
        return new DatabaseException("Database failure while persisting player, error: " + ex.getMessage() );
    }
}


package com.blackjack.ranking.infrastructure.listener;

import com.blackjack.player.domain.event.PlayerCreatedEvent;
import com.blackjack.ranking.domain.port.in.CreateRankingUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
@Async
@Component
public class PlayerCreatedListener {

    private final CreateRankingUseCase createRankingUseCase;

    public PlayerCreatedListener(CreateRankingUseCase createRankingUseCase) {
        this.createRankingUseCase = createRankingUseCase;
    }

    @EventListener
    public void onUserCreated(PlayerCreatedEvent event) {
        Mono.defer(() -> createRankingUseCase.execute(event.playerId(), event.name()))
                .subscribeOn(Schedulers.boundedElastic())
                .retryWhen(Retry.backoff(3, Duration.ofMillis(200))
                        .filter(ex -> ex instanceof OptimisticLockingFailureException)
                )
                .subscribe(
                        updated -> log.info("Ranking created for player {}: Score {}", updated.getPlayerId(), updated.getScore()),
                        error -> log.error("CRITICAL: Failed to create ranking for player {} after retries", event.playerId(), error)
                );

    }
}


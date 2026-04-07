package com.blackjack.ranking.infrastructure.listener;

import com.blackjack.game.domain.event.GameFinishedEvent;
import com.blackjack.game.domain.model.valueObject.GameStatus;
import com.blackjack.ranking.domain.port.in.UpdateRankingEntryUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
@Async
@Component
public class GameFinishedListener {

    private final UpdateRankingEntryUseCase updateRankingEntryUseCase;

    public GameFinishedListener(UpdateRankingEntryUseCase updateRankingEntryUseCase) {
        this.updateRankingEntryUseCase = updateRankingEntryUseCase;
    }

    @EventListener
    public void onGameFinished(GameFinishedEvent event) {
        Mono.defer(() -> updateRankingEntryUseCase.execute(event.playerId(), event.status()))
                .retryWhen(Retry.backoff(3, Duration.ofMillis(200))
                        .filter(ex -> ex instanceof OptimisticLockingFailureException)
                )
                .subscribe(
                        updated -> log.info("Ranking updated for player {}: Score {}", updated.getPlayerId(), updated.getScore()),
                        error -> log.error("CRITICAL: Failed to update ranking for player {} after retries", event.playerId(), error)
                );
    }
}



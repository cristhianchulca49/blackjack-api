package com.blackjack.ranking.infrastructure.listener;

import com.blackjack.player.domain.event.PlayerCreatedEvent;

import com.blackjack.player.domain.model.Name;
import com.blackjack.player.domain.model.PlayerId;
import com.blackjack.ranking.domain.model.Ranking;
import com.blackjack.ranking.domain.port.in.CreateRankingUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.OptimisticLockingFailureException;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerCreatedListenerTest {

    @Mock
    private CreateRankingUseCase createRankingUseCase;

    @InjectMocks
    private PlayerCreatedListener playerCreatedListener;

    @Test
    @DisplayName("Should execute use case when player created event is received")
    void shouldExecuteUseCaseOnEvent() {
        PlayerId playerId = PlayerId.generate();
        Name name = Name.of("Rosend");
        PlayerCreatedEvent event = new PlayerCreatedEvent(playerId, name);

        Ranking mockRanking = Ranking.create(playerId, name);

        when(createRankingUseCase.execute(playerId, name))
                .thenReturn(Mono.just(mockRanking));

        playerCreatedListener.onUserCreated(event);

        verify(createRankingUseCase, timeout(1000).times(1)).execute(playerId, name);
    }

    @Test
    @DisplayName("Should retry on OptimisticLockingFailureException")
    void shouldRetryOnLockFailure() {
        PlayerId playerId = PlayerId.generate();
        Name name = Name.of("Rosend");
        PlayerCreatedEvent event = new PlayerCreatedEvent(playerId, name);

        when(createRankingUseCase.execute(any(PlayerId.class), any(Name.class)))
                .thenReturn(Mono.error(new OptimisticLockingFailureException("Lock failed")))
                .thenReturn(Mono.just(Ranking.create(playerId, name)));

        playerCreatedListener.onUserCreated(event);

        verify(createRankingUseCase, timeout(1000).atLeast(2)).execute(eq(playerId), eq(name));
    }
}
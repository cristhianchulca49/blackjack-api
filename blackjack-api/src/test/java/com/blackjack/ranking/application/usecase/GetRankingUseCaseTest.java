package com.blackjack.ranking.application.usecase;

import com.blackjack.player.domain.model.Name;
import com.blackjack.player.domain.model.PlayerId;
import com.blackjack.ranking.domain.model.Ranking;
import com.blackjack.ranking.domain.port.out.RankingRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class GetRankingUseCaseTest {

    @Mock
    private RankingRepositoryPort rankingRepositoryPort;

    @InjectMocks
    private GetRankingUseCaseImpl getRankingUseCase;

    @Test
    @DisplayName("Should get ranking with limit")
    void getRankingTest() {
        int limit = 5;
        Ranking r1 = Ranking.create(PlayerId.generate(), Name.of("Player1"));
        Ranking r2 = Ranking.create(PlayerId.generate(), Name.of("Player2"));

        Mockito.when(rankingRepositoryPort.findTopByLimit(eq(limit))).thenReturn(Flux.just(r1, r2));

        StepVerifier.create(getRankingUseCase.execute(limit))
                .expectNext(r1)
                .expectNext(r2)
                .verifyComplete();

        Mockito.verify(rankingRepositoryPort).findTopByLimit(limit);
    }
}

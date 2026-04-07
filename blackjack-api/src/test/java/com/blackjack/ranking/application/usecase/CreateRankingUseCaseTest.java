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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class CreateRankingUseCaseTest {

    @Mock
    private RankingRepositoryPort rankingRepositoryPort;

    @InjectMocks
    private CreateRankingUseCaseImpl createRankingUseCase;

    @Test
    @DisplayName("Should create ranking for player")
    void createRankingTest() {
        PlayerId playerId = PlayerId.generate();
        Name name = Name.of("Player1");
        Ranking ranking = Ranking.create(playerId, name);

        Mockito.when(rankingRepositoryPort.save(any(Ranking.class))).thenReturn(Mono.just(ranking));

        StepVerifier.create(createRankingUseCase.execute(playerId, name))
                .expectNext(ranking)
                .verifyComplete();

        Mockito.verify(rankingRepositoryPort).save(any(Ranking.class));
    }
}

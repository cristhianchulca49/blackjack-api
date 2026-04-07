package com.blackjack.ranking.infrastructure.adapter.in.rest.dto;

import com.blackjack.player.domain.model.Name;
import com.blackjack.player.domain.model.PlayerId;
import com.blackjack.ranking.domain.model.Ranking;
import com.blackjack.ranking.domain.port.in.GetRankingUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

@WebFluxTest(RankingController.class)
class RankingControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private GetRankingUseCase getRankingUseCase;

    @Test
    @DisplayName("Should get ranking with limit")
    void getRankingTest() {
        int limit = 10;
        PlayerId playerId = PlayerId.generate();
        Name playerName = Name.of("Player1");

        Ranking ranking = Ranking.create(playerId, playerName);

        Mockito.when(getRankingUseCase.execute(limit))
                .thenReturn(Flux.just(ranking));

        webTestClient.get()
                .uri("/api/ranking/{limit}", limit)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].username").isEqualTo("Player1")
                .jsonPath("$[0].score").exists();
    }
}

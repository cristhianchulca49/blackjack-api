package com.blackjack.ranking.infrastructure.adapter.in.rest.dto;

import com.blackjack.ranking.domain.port.in.GetRankingUseCase;
import com.blackjack.ranking.infrastructure.adapter.in.rest.dto.response.RankingResponse;
import com.blackjack.ranking.infrastructure.mapper.RankingMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/ranking")
public class RankingController {
    private final GetRankingUseCase getRankingUseCase;

    public RankingController(GetRankingUseCase getRankingUseCase) {
        this.getRankingUseCase = getRankingUseCase;
    }

    @GetMapping("/{limit}")
    public Flux<RankingResponse> getRanking(@PathVariable int limit) {
        return getRankingUseCase.execute(limit)
                .map(RankingMapper::toResponse);
    }
}
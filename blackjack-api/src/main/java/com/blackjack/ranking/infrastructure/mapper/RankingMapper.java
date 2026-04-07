package com.blackjack.ranking.infrastructure.mapper;

import com.blackjack.player.domain.model.Name;
import com.blackjack.player.domain.model.PlayerId;
import com.blackjack.ranking.domain.model.Ranking;
import com.blackjack.ranking.domain.model.valueObject.Draws;
import com.blackjack.ranking.domain.model.valueObject.Position;
import com.blackjack.ranking.domain.model.valueObject.RankingId;
import com.blackjack.ranking.domain.model.valueObject.Score;
import com.blackjack.ranking.domain.model.valueObject.Wins;
import com.blackjack.ranking.infrastructure.adapter.in.rest.dto.response.RankingResponse;
import com.blackjack.ranking.infrastructure.adapter.out.persistence.entity.RankingEntity;

public class RankingMapper {

    public static RankingEntity toEntity(Ranking ranking) {
        return RankingEntity.builder()
                .id(ranking.getId().getValue())
                .playerId(ranking.getPlayerId().getValue())
                .playerName(ranking.getPlayerName().toString())
                .wins(ranking.getWins().getValue())
                .draws(ranking.getDraws().getValue())
                .score(ranking.getScore().getValue())
                .position(ranking.getPosition().getValue())
                .build();
    }

    public static Ranking toDomain(RankingEntity rankingEntity) {
        return Ranking.from(
                RankingId.reconstitute(rankingEntity.getId()),
                PlayerId.reconstitute(rankingEntity.getPlayerId()),
                Name.of(rankingEntity.getPlayerName()),
                Wins.of(rankingEntity.getWins()),
                Draws.of(rankingEntity.getDraws()),
                Score.of(rankingEntity.getScore()),
                Position.of(rankingEntity.getPosition())
        );
    }

    public static RankingResponse toResponse(Ranking ranking) {
        return RankingResponse.builder()
                .position(ranking.getPosition().getValue())
                .username(ranking.getPlayerName().toString())
                .totalWins(ranking.getWins().getValue())
                .totalDraws(ranking.getDraws().getValue())
                .build();
    }
}


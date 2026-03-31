package com.blackjack.ranking.infrastructure.adapter.out.persistence.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

@Table("ranking")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RankingEntity {

    @Id
    private String id;
    private String playerId;
    private String playerName;
    private int wins;
    private int draws;
    private int score;
    private int position;

    @Version
    private Long version;
}


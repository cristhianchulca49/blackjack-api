package com.blackjack.player.infrastructure.adapter.out.persistence.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("players")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PlayerEntity {

    @Id
    private String id;
    private String name;
}


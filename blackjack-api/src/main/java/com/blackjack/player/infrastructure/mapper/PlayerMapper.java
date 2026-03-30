package com.blackjack.player.infrastructure.mapper;

import com.blackjack.player.domain.model.Name;
import com.blackjack.player.domain.model.Player;
import com.blackjack.player.domain.model.PlayerId;
import com.blackjack.player.infrastructure.adapter.out.persistence.entity.PlayerEntity;

public class PlayerMapper {

    public static PlayerEntity toEntity(Player player) {
        return new PlayerEntity(
                player.getPlayerId().getValue(),
                player.getName().getValue()
        );
    }

    public static Player toDomain(PlayerEntity entity) {
        return new Player(
                PlayerId.of(entity.getId()),
                Name.of(entity.getName())
        );
    }
}


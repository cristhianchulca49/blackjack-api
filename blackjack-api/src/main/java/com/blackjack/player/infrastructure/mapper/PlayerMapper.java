package com.blackjack.player.infrastructure.mapper;

import com.blackjack.player.domain.model.Name;
import com.blackjack.player.domain.model.Player;
import com.blackjack.player.domain.model.PlayerId;
import com.blackjack.player.infrastructure.adapter.in.rest.dto.request.PlayerRequest;
import com.blackjack.player.infrastructure.adapter.in.rest.dto.response.PlayerResponse;
import com.blackjack.player.infrastructure.adapter.out.persistence.entity.PlayerEntity;

public class PlayerMapper {

    public static PlayerEntity toEntity(Player player) {
        return PlayerEntity.builder()
                .id(player.getPlayerId().getValue())
                .name(player.getName().toString())
                .build();
    }

    public static Player toDomain(PlayerEntity playerEntity) {
        return Player.reconstitute(
                PlayerId.reconstitute(playerEntity.getId()),
                Name.of(playerEntity.getName())
        );
    }

    public static Player toDomain(PlayerRequest playerRequest) {
        return Player.create(
                playerRequest.name()
        );
    }

    public static PlayerResponse toResponse(Player player) {
        return new PlayerResponse(player.getPlayerId().getValue(), player.getName().toString());
    }
}


package com.blackjack.game.infrastructure.mapper;

import com.blackjack.game.domain.model.Game;
import com.blackjack.game.domain.model.valueObject.GameId;
import com.blackjack.game.domain.model.valueObject.GameStatus;
import com.blackjack.game.infrastructure.adapter.out.persistence.document.GameDocument;
import com.blackjack.player.domain.model.PlayerId;

public class GameMapper {
    public static GameDocument toDocument(Game game) {
        return GameDocument.builder()
                .id(game.getId().toString())
                .playerId(game.getPlayerId().toString())
                .playerHand(HandMapper.toDocument(game.getPlayerHand()))
                .dealerHand(HandMapper.toDocument(game.getDealerHand()))
                .deck(DeckMapper.toDocument(game.getDeck()))
                .status(game.getStatus().name())
                .build();
    }

    public static Game toDomain(GameDocument gameDocument) {
        return Game.create(
                GameId.reconstitute(gameDocument.getId()),
                PlayerId.reconstitute(gameDocument.getPlayerId()),
                HandMapper.toDomain(gameDocument.getPlayerHand()),
                HandMapper.toDomain(gameDocument.getDealerHand()),
                DeckMapper.toDomain(gameDocument.getDeck()),
                GameStatus.valueOf(gameDocument.getStatus()));
    }
}

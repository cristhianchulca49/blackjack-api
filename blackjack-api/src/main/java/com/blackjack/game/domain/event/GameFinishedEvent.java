package com.blackjack.game.domain.event;

import com.blackjack.game.domain.model.valueObject.GameId;
import com.blackjack.game.domain.model.valueObject.GameStatus;
import com.blackjack.player.domain.model.PlayerId;


public record GameFinishedEvent (PlayerId playerId, GameStatus status) {

}


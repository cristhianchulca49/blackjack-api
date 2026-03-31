package com.blackjack.player.domain.event;

import com.blackjack.player.domain.model.Name;
import com.blackjack.player.domain.model.PlayerId;

public record PlayerCreatedEvent(PlayerId playerId, Name name) {
}


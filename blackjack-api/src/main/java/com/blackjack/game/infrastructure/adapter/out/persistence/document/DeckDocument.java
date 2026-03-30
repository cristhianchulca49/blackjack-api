package com.blackjack.game.infrastructure.adapter.out.persistence.document;

import java.util.List;

public record DeckDocument(List<CardDocument> cards) {
}
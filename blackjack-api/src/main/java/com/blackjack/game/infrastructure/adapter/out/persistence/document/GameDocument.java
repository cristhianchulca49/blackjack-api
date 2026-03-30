package com.blackjack.game.infrastructure.adapter.out.persistence.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "games")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameDocument {

    @Id
    private String id;
    private String playerId;
    private HandDocument dealerHand;
    private HandDocument playerHand;
    private DeckDocument deck;
    private String status;
}
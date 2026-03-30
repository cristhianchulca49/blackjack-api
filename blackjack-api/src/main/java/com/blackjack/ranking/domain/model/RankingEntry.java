package com.blackjack.ranking.domain.model;

import com.blackjack.player.domain.model.Name;
import com.blackjack.player.domain.model.PlayerId;
import com.blackjack.ranking.domain.model.valueObject.Position;
import com.blackjack.ranking.domain.model.valueObject.RankingId;
import com.blackjack.ranking.domain.model.valueObject.Wins;

public class RankingEntry {
    private final RankingId id;
    private final PlayerId playerId;
    private final Name playerName;
    private final Wins wins;
    private final Position position;

    private RankingEntry(RankingId id, PlayerId playerId, Name playerName, Wins wins, Position position) {
        this.id = id;
        this.playerId = playerId;
        this.playerName = playerName;
        this.wins = wins;
        this.position = position;
    }

    public static RankingEntry create(PlayerId playerId, Name playerName) {
        return new RankingEntry(
            RankingId.generate(),
            playerId,
            playerName,
            Wins.zero(),
            Position.of(Integer.MAX_VALUE)
        );
    }

    public static RankingEntry from(RankingId id, PlayerId playerId, Name playerName, Wins wins, Position position) {
        return new RankingEntry(id, playerId, playerName, wins, position);
    }

    public RankingEntry withWins(Wins newWins) {
        return new RankingEntry(this.id, this.playerId, this.playerName, newWins, this.position);
    }

    public RankingEntry withPosition(Position newPosition) {
        return new RankingEntry(this.id, this.playerId, this.playerName, this.wins, newPosition);
    }

    public RankingEntry incrementWins() {
        return new RankingEntry(this.id, this.playerId, this.playerName, this.wins.increment(), this.position);
    }

    public RankingId getId() {
        return id;
    }

    public PlayerId getPlayerId() {
        return playerId;
    }

    public Name getPlayerName() {
        return playerName;
    }

    public Wins getWins() {
        return wins;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RankingEntry other)) return false;
        return playerId.equals(other.playerId);
    }

    @Override
    public int hashCode() {
        return playerId.hashCode();
    }

    @Override
    public String toString() {
        return "RankingEntry{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", playerName=" + playerName +
                ", wins=" + wins +
                ", position=" + position +
                '}';
    }
}


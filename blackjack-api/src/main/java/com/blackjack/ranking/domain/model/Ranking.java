package com.blackjack.ranking.domain.model;

import com.blackjack.player.domain.model.Name;
import com.blackjack.player.domain.model.PlayerId;
import com.blackjack.game.domain.model.valueObject.GameStatus;
import com.blackjack.ranking.domain.model.valueObject.Draws;
import com.blackjack.ranking.domain.model.valueObject.Position;
import com.blackjack.ranking.domain.model.valueObject.RankingId;
import com.blackjack.ranking.domain.model.valueObject.Score;
import com.blackjack.ranking.domain.model.valueObject.Wins;

public class Ranking {
    private final RankingId id;
    private final PlayerId playerId;
    private final Name playerName;
    private final Wins wins;
    private final Draws draws;
    private final Score score;
    private final Position position;

    private Ranking(RankingId id, PlayerId playerId, Name playerName, Wins wins, Draws draws, Score score, Position position) {
        this.id = id;
        this.playerId = playerId;
        this.playerName = playerName;
        this.wins = wins;
        this.draws = draws;
        this.score = score;
        this.position = position;
    }

    public static Ranking create(PlayerId playerId, Name playerName) {
        Wins w = Wins.zero();
        Draws d = Draws.zero();
        Score s = Score.calculate(w, d);
        return new Ranking(
            RankingId.generate(),
            playerId,
            playerName,
            w,
            d,
            s,
            Position.of(Integer.MAX_VALUE)
        );
    }

    public static Ranking from(RankingId id, PlayerId playerId, Name playerName, Wins wins, Draws draws, Score score, Position position) {
        return new Ranking(id, playerId, playerName, wins, draws, score, position);
    }

    public Ranking incrementWins() {
        Wins newWins = this.wins.increment();
        Draws currentDraws = this.draws;
        Score newScore = Score.calculate(newWins, currentDraws);
        return new Ranking(this.id, this.playerId, this.playerName, newWins, currentDraws, newScore, this.position);
    }

    public Ranking recordResult(GameStatus status) {
        return switch (status) {
            case PLAYER_WINS -> this.incrementWins();
            case DRAW -> {
                Draws newDraws = this.draws.increment();
                yield new Ranking(this.id, this.playerId, this.playerName, this.wins, newDraws, Score.calculate(this.wins, newDraws), this.position);
            }
            default -> this;
        };
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

    public Draws getDraws() {
        return draws;
    }

    public Score getScore() {
        return score;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ranking other)) return false;
        return playerId.equals(other.playerId);
    }

    @Override
    public int hashCode() {
        return playerId.hashCode();
    }

    @Override
    public String toString() {
        return "Ranking{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", playerName=" + playerName +
                ", wins=" + wins +
                ", position=" + position +
                '}';
    }
}


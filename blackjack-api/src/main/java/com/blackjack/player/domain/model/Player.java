package com.blackjack.player.domain.model;

public class Player {
    private final PlayerId playerId;
    private Name name;


    private Player(PlayerId playerId, Name name) {
        this.playerId = playerId;
        this.name = name;
    }

    public static Player create(String name) {
        return new Player(PlayerId.generate(), Name.of(name));
    }

    public static Player reconstitute(PlayerId playerId, Name name) {
        return new Player(playerId, name);
    }

    public Player changeName(String newName) {
        return new Player(this.playerId, Name.of(newName));
    }

    public Name getName() {
        return this.name;
    }

    public PlayerId getPlayerId() {
        return this.playerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player other)) return false;
        return playerId.equals(other.playerId);
    }

    @Override
    public int hashCode() {
        return playerId.hashCode();
    }

    @Override
    public String toString() {
        return "Player{playerId='" + playerId + "', name=" + name + "}";
    }
}

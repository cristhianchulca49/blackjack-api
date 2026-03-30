package com.blackjack.player.domain.model;


import com.blackjack.player.domain.model.exception.InvalidNameException;

public class Name {

    private final String name;

    private Name(String name) {
        this.name = name;
    }

    public static Name of(String name) {
        if (name == null) {
            throw new InvalidNameException("Name cannot be null");
        }
        if (name.isBlank()) {
            throw new InvalidNameException("Name cannot be blank");
        }
        return new Name(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Name other)) return false;
        return name.equals(other.name);
    }

    @Override
    public int hashCode() { return name.hashCode(); }

    @Override
    public String toString() { return name; }
}
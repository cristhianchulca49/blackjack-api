package com.blackjack.player.infrastructure.adapter.in.rest.dto.response;

import lombok.Getter;

@Getter
public class PlayerResponse {
    private final String id;
    private final String name;

    public PlayerResponse(String id, String name) {
        this.id = id;
        this.name = name;
    }

}

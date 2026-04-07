package com.blackjack.game.infrastructure.adapter.in.rest.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PlayGameRequest(
        @NotBlank
        String action
) {
}


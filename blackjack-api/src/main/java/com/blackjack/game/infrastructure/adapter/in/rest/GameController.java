package com.blackjack.game.infrastructure.adapter.in.rest;

import com.blackjack.game.domain.model.valueObject.GameId;
import com.blackjack.game.domain.port.in.*;
import com.blackjack.game.infrastructure.adapter.in.rest.dto.request.PlayGameRequest;
import com.blackjack.game.infrastructure.adapter.in.rest.dto.response.GameResponse;
import com.blackjack.game.infrastructure.mapper.GameMapper;
import com.blackjack.player.domain.model.PlayerId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {

    private final CreateGameUseCase createGameUseCase;
    private final GetGameUseCase getGameUseCase;
    private final PlayGameUseCase playGameUseCase;
    private final DeleteGameUseCase deleteGameUseCase;

    @PostMapping("/player/{playerId}")
    public Mono<ResponseEntity<GameResponse>> createGame(
            @PathVariable String playerId,
            UriComponentsBuilder ucb
    ) {
        return Mono.just(PlayerId.reconstitute(playerId))
                .flatMap(createGameUseCase::execute)
                .map(game -> {
                    URI location = ucb.path("/api/games/{id}")
                            .buildAndExpand(game.getId().getValue())
                            .toUri();
                    return ResponseEntity.created(location).body(GameMapper.toResponse(game));
                });
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<GameResponse>> getGame(@PathVariable String id) {
        return Mono.just(GameId.reconstitute(id))
                .flatMap(getGameUseCase::execute)
                .map(game -> ResponseEntity.ok(GameMapper.toResponse(game)));
    }

    @PostMapping("/{id}/play")
    public Mono<ResponseEntity<GameResponse>> play(
            @PathVariable String id,
            @Valid @RequestBody PlayGameRequest request
    ) {
        return Mono.just(GameId.reconstitute(id))
                .flatMap(gameId -> playGameUseCase.execute(gameId, request.getAction()))
                .map(game -> ResponseEntity.ok(GameMapper.toResponse(game)));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return Mono.just(GameId.reconstitute(id))
                .flatMap(deleteGameUseCase::execute)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
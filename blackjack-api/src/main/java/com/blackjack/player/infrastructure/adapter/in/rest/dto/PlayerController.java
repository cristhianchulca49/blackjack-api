package com.blackjack.player.infrastructure.adapter.in.rest.dto;

import com.blackjack.game.infrastructure.mapper.GameMapper;
import com.blackjack.player.domain.port.in.CreatePlayerUseCase;
import com.blackjack.player.domain.port.in.UpdatePlayerNameUseCase;
import com.blackjack.player.domain.port.in.DeletePlayerUseCase;
import com.blackjack.player.infrastructure.adapter.in.rest.dto.request.PlayerRequest;
import com.blackjack.player.infrastructure.adapter.in.rest.dto.response.PlayerResponse;
import com.blackjack.player.infrastructure.mapper.PlayerMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    private final CreatePlayerUseCase createPlayerUseCase;
    private final UpdatePlayerNameUseCase updatePlayerNameUseCase;
    private final DeletePlayerUseCase deletePlayerUseCase;

    public PlayerController(CreatePlayerUseCase createPlayerUseCase,
                            UpdatePlayerNameUseCase updatePlayerNameUseCase,
                            DeletePlayerUseCase deletePlayerUseCase) {
        this.createPlayerUseCase = createPlayerUseCase;
        this.updatePlayerNameUseCase = updatePlayerNameUseCase;
        this.deletePlayerUseCase = deletePlayerUseCase;
    }

    @PostMapping
    public Mono<ResponseEntity<PlayerResponse>> create(
            @Valid @RequestBody PlayerRequest request,
            UriComponentsBuilder ucb
    ) {
        return Mono.just(request.name())
                .flatMap(createPlayerUseCase::execute)
                .map(player -> {
                    URI location = ucb.path("/api/player/{id}")
                            .buildAndExpand(player.getPlayerId().getValue())
                            .toUri();
                    return ResponseEntity.created(location).body(PlayerMapper.toResponse(player));
                });
    }

        @PutMapping("/{playerId}")
        public Mono<ResponseEntity<PlayerResponse>> update (@PathVariable String playerId,
                @Valid @RequestBody PlayerRequest request){
            return updatePlayerNameUseCase.execute(com.blackjack.player.domain.model.PlayerId.reconstitute(playerId), request.name())
                    .map(PlayerMapper::toResponse)
                    .map(ResponseEntity::ok);
        }

        @DeleteMapping("/{playerId}")
        public Mono<ResponseEntity<Void>> delete (@PathVariable String playerId){
            return deletePlayerUseCase.execute(com.blackjack.player.domain.model.PlayerId.reconstitute(playerId))
                    .thenReturn(ResponseEntity.noContent().build());
        }
    }

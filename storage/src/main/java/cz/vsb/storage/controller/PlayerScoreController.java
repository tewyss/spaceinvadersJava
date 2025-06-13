package cz.vsb.storage.controller;

import cz.vsb.storage.dto.CreateScoreRequest;
import cz.vsb.storage.dto.ScoreDto;
import cz.vsb.storage.dto.UpdateScoreRequest;
import cz.vsb.storage.entity.GameSession;
import cz.vsb.storage.entity.PlayerScore;
import cz.vsb.storage.repository.GameSessionRepository;
import cz.vsb.storage.repository.PlayerScoreRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/scores")
@Validated
public class PlayerScoreController {

    private final PlayerScoreRepository scoreRepo;
    private final GameSessionRepository sessionRepo;

    public PlayerScoreController(
            PlayerScoreRepository scoreRepo,
            GameSessionRepository sessionRepo
    ) {
        this.scoreRepo = scoreRepo;
        this.sessionRepo = sessionRepo;
    }

    @GetMapping
    @Operation(summary = "Get all scores as a JSON array")
    public ScoreDto[] all() {
        return scoreRepo.findAll().stream()
                .map(this::toDto)
                .toArray(ScoreDto[]::new);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new score")
    @ApiResponse(responseCode = "201", description = "Score created")
    public ScoreDto create(
            @Valid @RequestBody CreateScoreRequest req
    ) {
        GameSession session = sessionRepo.findById(req.sessionId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Invalid session ID"));

        PlayerScore entity = new PlayerScore();
        entity.setSession(session);
        entity.setScore(req.score());
        entity.setRecordedAt(LocalDateTime.now());

        PlayerScore saved = scoreRepo.save(entity);
        return toDto(saved);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing score")
    public ScoreDto update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateScoreRequest req
    ) {
        PlayerScore existing = scoreRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Score not found with id " + id));

        existing.setScore(req.score());
        PlayerScore saved = scoreRepo.save(existing);
        return toDto(saved);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a score by ID")
    public void delete(@PathVariable Long id) {
        if (!scoreRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Score not found");
        }
        scoreRepo.deleteById(id);
    }

    private ScoreDto toDto(PlayerScore e) {
        return new ScoreDto(
                e.getId(),
                e.getSession().getPlayer().getName(),
                e.getScore(),
                e.getRecordedAt()
        );
    }
}

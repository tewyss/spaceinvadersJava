package cz.vsb.storage.controller;

import cz.vsb.storage.dto.CreateSessionRequest;
import cz.vsb.storage.dto.SessionDto;
import cz.vsb.storage.entity.GameSession;
import cz.vsb.storage.entity.Player;
import cz.vsb.storage.repository.GameSessionRepository;
import cz.vsb.storage.repository.PlayerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class GameSessionController {
    private final GameSessionRepository sessionRepo;
    private final PlayerRepository playerRepo;

    public GameSessionController(
            GameSessionRepository sessionRepo,
            PlayerRepository playerRepo
    ) {
        this.sessionRepo = sessionRepo;
        this.playerRepo  = playerRepo;
    }

    @GetMapping
    public List<GameSession> all() {
        return sessionRepo.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionDto create(@Valid @RequestBody CreateSessionRequest req) {
        Player player = playerRepo.findById(req.playerId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Invalid player ID"));

        GameSession session = new GameSession();
        session.setPlayer(player);
        session.setStartTime(req.startTime());
        session.setEndTime(req.endTime());
        session = sessionRepo.save(session);

        return new SessionDto(
                session.getId(),
                session.getPlayer().getId(),
                session.getStartTime(),
                session.getEndTime()
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        sessionRepo.deleteById(id);
    }
}

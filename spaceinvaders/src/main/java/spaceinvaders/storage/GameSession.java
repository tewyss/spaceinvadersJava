package spaceinvaders.storage;

import java.time.LocalDateTime;

public record GameSession(
        Long id,
        Long playerId,
        LocalDateTime startTime,
        LocalDateTime endTime
) {}

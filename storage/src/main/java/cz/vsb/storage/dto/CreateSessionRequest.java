package cz.vsb.storage.dto;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;

public record CreateSessionRequest(
        @NotNull Long playerId,
        @NotNull LocalDateTime startTime,
        @NotNull LocalDateTime endTime
) {}

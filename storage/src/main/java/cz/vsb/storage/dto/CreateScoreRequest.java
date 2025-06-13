package cz.vsb.storage.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateScoreRequest(
        @NotNull(message = "Session ID is required")
        Long sessionId,

        @NotNull(message = "Score is required")
        @Min(value = 0, message = "Score must be non-negative")
        Integer score
) {}

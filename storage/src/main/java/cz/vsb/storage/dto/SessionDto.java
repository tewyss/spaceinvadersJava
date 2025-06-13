package cz.vsb.storage.dto;

import java.time.LocalDateTime;

public record SessionDto(
        Long id,
        Long playerId,
        LocalDateTime startTime,
        LocalDateTime endTime
) {}

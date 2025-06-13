package cz.vsb.storage.dto;

import java.time.LocalDateTime;

public record ScoreDto(
        Long id,
        String playerName,
        Integer score,
        LocalDateTime recordedAt
) {}

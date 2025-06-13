package spaceinvaders.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerScore {
    private Long id;
    private String playerName;
    private int score;
    private LocalDateTime recordedAt;

}

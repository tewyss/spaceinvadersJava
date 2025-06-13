package cz.vsb.storage.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "player_score")
public class PlayerScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer score;
    private LocalDateTime recordedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "session_id")
    @JsonBackReference
    private GameSession session;

    public PlayerScore() {}

    public void setScore(Integer s)             { this.score      = s; }
    public void setRecordedAt(LocalDateTime dt) { this.recordedAt = dt; }
    public void setSession(GameSession sess)    { this.session    = sess; }
}

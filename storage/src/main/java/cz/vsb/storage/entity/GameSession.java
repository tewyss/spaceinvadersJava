package cz.vsb.storage.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@Table(name = "game_session")
public class GameSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @ManyToOne(optional = false)
    @JoinColumn(name = "player_id")
    @JsonBackReference
    private Player player;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PlayerScore> scores;

    public GameSession() {}

    public void setStartTime(LocalDateTime t) { this.startTime = t; }
    public void setEndTime(LocalDateTime t)   { this.endTime   = t; }
    public void setPlayer(Player p)            { this.player    = p; }
    public void setScores(List<PlayerScore> l){ this.scores    = l; }
}

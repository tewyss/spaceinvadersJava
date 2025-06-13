// cz.vsb.storage.entity.Player.java
package cz.vsb.storage.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Getter
@Entity
@Table(name = "player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<GameSession> sessions;

    public Player() {}
    public Player(String name) { this.name = name; }

    public void setName(String name) { this.name = name; }
    public void setSessions(List<GameSession> sessions) { this.sessions = sessions; }
}

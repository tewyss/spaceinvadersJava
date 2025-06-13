package cz.vsb.storage.repository;

import cz.vsb.storage.entity.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameSessionRepository extends JpaRepository<GameSession, Long> {

}

package cz.vsb.storage.repository;

import cz.vsb.storage.entity.PlayerScore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerScoreRepository extends JpaRepository<PlayerScore, Long> {

}

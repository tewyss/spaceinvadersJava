package cz.vsb.storage.repository;

import cz.vsb.storage.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {

}
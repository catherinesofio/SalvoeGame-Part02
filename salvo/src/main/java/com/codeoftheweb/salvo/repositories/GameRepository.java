package com.codeoftheweb.salvo.repositories;

import com.codeoftheweb.salvo.entities.Game;
import com.codeoftheweb.salvo.entities.GamePlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface GameRepository extends JpaRepository<Game, Long> {
    Game findByGamePlayer(@Param("gamePlayer")GamePlayer gamePlayer);
}

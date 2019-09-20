package com.codeoftheweb.salvo.repositories;

import com.codeoftheweb.salvo.entities.Game;
import com.codeoftheweb.salvo.entities.GameStateMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface GameStateMachineRepository extends JpaRepository<GameStateMachine, Long> {
    GameStateMachine findByGame(@Param("game") Game game);
}

package com.codeoftheweb.salvo.repositories;

import com.codeoftheweb.salvo.entities.GameLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface GameLogRepository extends JpaRepository<GameLog, Long> {
}
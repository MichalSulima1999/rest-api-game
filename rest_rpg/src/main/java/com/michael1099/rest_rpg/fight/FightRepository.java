package com.michael1099.rest_rpg.fight;

import com.michael1099.rest_rpg.fight.model.Fight;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FightRepository extends JpaRepository<Fight, Long> {

    @EntityGraph(Fight.TEST_GRAPH)
    Optional<Fight> readById(Long id);
}

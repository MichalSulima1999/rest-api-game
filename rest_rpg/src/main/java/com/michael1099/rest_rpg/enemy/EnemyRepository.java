package com.michael1099.rest_rpg.enemy;

import com.michael1099.rest_rpg.enemy.model.Enemy;
import com.michael1099.rest_rpg.exceptions.EnemyDoesNotExistException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnemyRepository extends JpaRepository<Enemy, Long> {

    default Enemy getById(long id) {
        return findById(id).orElseThrow(EnemyDoesNotExistException::new);
    }

    boolean existsByName(String name);
}

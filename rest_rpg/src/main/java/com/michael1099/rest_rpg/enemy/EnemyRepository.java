package com.michael1099.rest_rpg.enemy;

import com.michael1099.rest_rpg.enemy.model.Enemy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnemyRepository extends JpaRepository<Enemy, Long> {

    boolean existsByName(String name);
}

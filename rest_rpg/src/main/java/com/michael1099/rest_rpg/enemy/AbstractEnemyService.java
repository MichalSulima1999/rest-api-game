package com.michael1099.rest_rpg.enemy;

import com.michael1099.rest_rpg.skill.SkillRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractEnemyService implements EnemyService {

    protected final EnemyRepository enemyRepository;
    protected final StrategyElementRepository strategyElementRepository;
    protected final EnemyMapper enemyMapper;
    protected final SkillRepository skillRepository;
}

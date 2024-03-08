package com.michael1099.rest_rpg.fight;

import com.michael1099.rest_rpg.enemy.model.Enemy;
import com.michael1099.rest_rpg.fight.helpers.FightEffectsSingleton;
import com.michael1099.rest_rpg.fight.model.Fight;
import com.michael1099.rest_rpg.statistics.Statistics;
import org.openapitools.model.FightActionResponse;

import java.util.Random;

public class EnemyUsePotion implements EnemyAction {

    private final FightEffectsSingleton fightEffectsSingleton;
    private final FightActionResponse response;
    private final Statistics playerStatistics;
    private final Enemy enemy;
    private final Fight fight;

    public EnemyUsePotion(FightActionResponse response, Statistics playerStatistics, Enemy enemy, Fight fight) {
        this.fightEffectsSingleton = FightEffectsSingleton.getInstance();
        this.response = response;
        this.playerStatistics = playerStatistics;
        this.enemy = enemy;
        this.fight = fight;
    }

    @Override
    public void perform() {
        if (enemy.getNumberOfPotions() > 0) {
            fight.healEnemy();
            enemy.usePotion();
        } else {
            var successfulHit = new Random().nextFloat(0, 100) > playerStatistics.getDodgeChance();
            response.setEnemyHit(false);
            response.setEnemyDamage(0);
            if (successfulHit) {
                var enemyDamage = Math.max(1, enemy.getDamage() - Math.round(playerStatistics.getArmor() * fightEffectsSingleton.getPlayerDefenceMultiplier().get()));
                playerStatistics.takeDamage(enemyDamage);
                response.setEnemyHit(true);
                response.setEnemyDamage(enemyDamage);
                response.setPlayerCurrentHp(playerStatistics.getCurrentHp());
            }
        }
    }
}

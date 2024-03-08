package com.michael1099.rest_rpg.fight;

import com.michael1099.rest_rpg.enemy.model.Enemy;
import com.michael1099.rest_rpg.fight.helpers.FightEffectsSingleton;
import com.michael1099.rest_rpg.statistics.Statistics;
import org.openapitools.model.FightActionResponse;

import java.util.Random;

public class EnemyNormalAttack implements EnemyAction {

    private final FightEffectsSingleton fightEffectsSingleton;
    private final FightActionResponse response;
    private final Statistics playerStatistics;
    private final Enemy enemy;

    public EnemyNormalAttack(FightActionResponse response, Statistics playerStatistics, Enemy enemy) {
        this.response = response;
        this.playerStatistics = playerStatistics;
        this.enemy = enemy;
        this.fightEffectsSingleton = FightEffectsSingleton.getInstance();
    }

    @Override
    public void perform() {
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

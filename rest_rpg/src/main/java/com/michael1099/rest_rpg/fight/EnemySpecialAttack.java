package com.michael1099.rest_rpg.fight;

import com.michael1099.rest_rpg.enemy.model.Enemy;
import com.michael1099.rest_rpg.fight.helpers.FightEffectsSingleton;
import com.michael1099.rest_rpg.fight.model.Fight;
import com.michael1099.rest_rpg.fight_effect.FightEffect;
import com.michael1099.rest_rpg.statistics.Statistics;
import org.openapitools.model.FightActionResponse;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class EnemySpecialAttack implements EnemyAction {

    private final FightEffectsSingleton fightEffectsSingleton;
    private final FightActionResponse response;
    private final Statistics playerStatistics;
    private final Enemy enemy;
    private final Fight fight;

    public EnemySpecialAttack(FightActionResponse response, Statistics playerStatistics, Enemy enemy, Fight fight) {
        this.response = response;
        this.playerStatistics = playerStatistics;
        this.enemy = enemy;
        this.fightEffectsSingleton = FightEffectsSingleton.getInstance();
        this.fight = fight;
    }

    @Override
    public void perform() {
        var skill = enemy.getSkill();
        response.setEnemyHit(false);
        var successfulHit = new Random().nextFloat(0, 100) > playerStatistics.getDodgeChance();
        if (fight.getEnemyCurrentMana() < skill.getManaCost()) {
            response.setEnemyHit(false);
            response.setEnemyDamage(0);
            if (successfulHit) {
                var enemyDamage = Math.max(1, enemy.getDamage() - Math.round(playerStatistics.getArmor() * fightEffectsSingleton.getPlayerDefenceMultiplier().get()));
                playerStatistics.takeDamage(enemyDamage);
                response.setEnemyHit(true);
                response.setEnemyDamage(enemyDamage);
                response.setPlayerCurrentHp(playerStatistics.getCurrentHp());
            }
        } else {
            if (successfulHit) {
                int effectDuration = skill.getEffectDuration();
                var enemyDamage = Math.max(1,
                        Math.round((enemy.getSkill().getMultiplier() + effectDuration * enemy.getSkillLevel()) *
                                enemy.getDamage() + enemy.getDamage() - playerStatistics.getArmor() * fightEffectsSingleton.getPlayerDefenceMultiplier().get()));
                playerStatistics.takeDamage(enemyDamage);
                fight.enemyUseMana();
                response.setEnemyDamage(enemyDamage);
                response.setEnemyHit(true);
                response.setPlayerCurrentHp(playerStatistics.getCurrentHp());
                if (skill.getEffect() != null) {
                    var effects = Optional.ofNullable(fight.getFightEffects()).orElse(new HashSet<>()).stream()
                            .filter(fightEffect -> fightEffect.getDuration() <= 0).collect(Collectors.toSet());
                    FightEffect fightEffect = new FightEffect();
                    if (!effects.isEmpty()) {
                        fightEffect = effects.stream().findFirst().get();
                    }
                    fightEffect.setFight(fight);
                    fightEffect.setDuration(effectDuration);
                    fightEffect.setPlayerEffect(true);
                    fightEffect.setSkillEffect(skill.getEffect());
                    fightEffect.setEffectMultiplier(skill.getFinalEffectMultiplier(enemy.getSkillLevel()));
                    fight.addFightEffect(fightEffect);
                }
            }
        }
    }
}

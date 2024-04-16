package com.michael1099.rest_rpg.fight;

import com.michael1099.rest_rpg.enemy.model.Enemy;
import com.michael1099.rest_rpg.fight.helpers.FightEffectsSingleton;
import com.michael1099.rest_rpg.fight.model.Fight;
import com.michael1099.rest_rpg.fight_effect.FightEffect;
import com.michael1099.rest_rpg.skill.model.Skill;
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
        var successfulHit = isSuccessfulHit();
        var skill = enemy.getSkill();
        var playerCurrentHp = playerStatistics.getCurrentHp();

        if (fight.getEnemyCurrentMana() >= skill.getManaCost()) {
            useSpecialAttack(skill, playerCurrentHp, successfulHit);
        } else if (successfulHit) {
            useNormalAttack(playerCurrentHp);
        } else {
            response.setEnemyHit(false);
            response.setEnemyDamage(0);
        }
    }

    private void useSpecialAttack(Skill skill, int playerCurrentHp, boolean successfulHit) {
        if (successfulHit) {
            var effectDuration = skill.getEffectDuration();
            var enemyDamage = calculateEnemyDamageWithMana(skill, effectDuration);
            playerStatistics.takeDamage(enemyDamage);
            fight.enemyUseMana();
            response.setEnemyDamage(enemyDamage);
            response.setEnemyHit(true);
            response.setPlayerCurrentHp(playerCurrentHp);
            applySkillEffect(skill, effectDuration);
        }
    }

    private void useNormalAttack(int playerCurrentHp) {
        var enemyDamage = calculateEnemyDamage();
        playerStatistics.takeDamage(enemyDamage);
        response.setEnemyHit(true);
        response.setEnemyDamage(enemyDamage);
        response.setPlayerCurrentHp(playerCurrentHp);
    }

    private boolean isSuccessfulHit() {
        return new Random().nextFloat(0, 100) > playerStatistics.getDodgeChance();
    }

    private int calculateEnemyDamageWithMana(Skill skill, int effectDuration) {
        var playerArmor = playerStatistics.getArmor();
        var playerDefenceMultiplier = fightEffectsSingleton.getPlayerDefenceMultiplier().get();
        return Math.max(1,
                Math.round((skill.getMultiplier() + effectDuration * enemy.getSkillLevel()) *
                        enemy.getDamage() + enemy.getDamage() - playerArmor * playerDefenceMultiplier));
    }

    private int calculateEnemyDamage() {
        var playerArmor = playerStatistics.getArmor();
        var playerDefenceMultiplier = fightEffectsSingleton.getPlayerDefenceMultiplier().get();
        return Math.max(1, enemy.getDamage() - Math.round(playerArmor * playerDefenceMultiplier));
    }

    private void applySkillEffect(Skill skill, int effectDuration) {
        if (skill.getEffect() != null) {
            var effects = Optional.ofNullable(fight.getFightEffects()).orElse(new HashSet<>()).stream()
                    .filter(fightEffect -> fightEffect.getDuration() <= 0).collect(Collectors.toSet());
            FightEffect fightEffect = effects.isEmpty() ? new FightEffect() : effects.stream().findFirst().get();

            fightEffect.setFight(fight);
            fightEffect.setDuration(effectDuration);
            fightEffect.setPlayerEffect(true);
            fightEffect.setSkillEffect(skill.getEffect());
            fightEffect.setEffectMultiplier(skill.getFinalEffectMultiplier(enemy.getSkillLevel()));

            fight.addFightEffect(fightEffect);
        }
    }
}

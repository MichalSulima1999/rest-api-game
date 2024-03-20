package com.michael1099.rest_rpg.fight_effect.strategy;

import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.enemy.model.Enemy;
import com.michael1099.rest_rpg.fight.model.Fight;
import com.michael1099.rest_rpg.fight_effect.FightEffect;

import java.util.Optional;

public class BurningStrategy implements FightEffectStrategy {

    @Override
    public void applyEffect(FightEffect effect, Fight fight, Character character) {
        if (effect.isPlayerEffect()) {
            character.getStatistics().takeDamage(
                    Math.round(character.getStatistics().getMaxHp() * effect.getEffectMultiplier()));
        } else {
            var enemyMaxHp = Optional.ofNullable(fight.getEnemy()).map(Enemy::getHp).orElseThrow();
            fight.dealDamageToEnemy(Math.round(enemyMaxHp * effect.getEffectMultiplier()));
        }
    }
}

package com.michael1099.rest_rpg.fight_effect.strategy;

import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.fight.helpers.FightEffectsSingleton;
import com.michael1099.rest_rpg.fight.model.Fight;
import com.michael1099.rest_rpg.fight_effect.FightEffect;

public class WeaknessStrategy implements FightEffectStrategy {

    @Override
    public void applyEffect(FightEffect effect, Fight fight, Character character) {
        var fightEffectsSingleton = FightEffectsSingleton.getInstance();
        if (effect.isPlayerEffect()) {
            fightEffectsSingleton.getPlayerDamageMultiplier().set(Math.max(0.1f, fightEffectsSingleton.getPlayerDamageMultiplier().get() - effect.getEffectMultiplier()));
        } else {
            fightEffectsSingleton.getEnemyDamageMultiplier().set(Math.max(0.1f, fightEffectsSingleton.getEnemyDamageMultiplier().get() - effect.getEffectMultiplier()));
        }
    }
}

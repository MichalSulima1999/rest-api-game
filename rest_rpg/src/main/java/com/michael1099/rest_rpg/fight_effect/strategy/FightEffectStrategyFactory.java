package com.michael1099.rest_rpg.fight_effect.strategy;

import org.openapitools.model.SkillEffect;

import java.util.HashMap;
import java.util.Map;

public class FightEffectStrategyFactory {

    private static final Map<SkillEffect, FightEffectStrategy> strategies = new HashMap<>();

    static {
        strategies.put(SkillEffect.BLEEDING, new BleedingStrategy());
        strategies.put(SkillEffect.BURNING, new BurningStrategy());
        strategies.put(SkillEffect.STUNNED, new StunnedStrategy());
        strategies.put(SkillEffect.WEAKNESS, new WeaknessStrategy());
        strategies.put(SkillEffect.DAMAGE_BOOST, new DamageBoostStrategy());
        strategies.put(SkillEffect.DEFENCE_BOOST, new DefenceBoostStrategy());
        strategies.put(SkillEffect.DAMAGE_DEFENCE_BOOST, new DamageDefenseBoostStrategy());
    }

    public static FightEffectStrategy getStrategy(SkillEffect effectType) {
        return strategies.get(effectType);
    }
}

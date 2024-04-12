package com.michael1099.rest_rpg.fight;

import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.fight.helpers.FightAction;
import com.michael1099.rest_rpg.fight.helpers.FightEffectsSingleton;
import com.michael1099.rest_rpg.fight.helpers.NormalAttack;
import com.michael1099.rest_rpg.fight.helpers.SpecialAttack;
import com.michael1099.rest_rpg.fight.helpers.UsePotion;
import com.michael1099.rest_rpg.fight.model.FightActionRequestDto;
import com.michael1099.rest_rpg.skill.SkillRepository;
import org.openapitools.model.FightActionResponse;

public class FightEffectsManagerImplementation implements FightEffectsManager {

    private final FightEffectsSingleton fightEffectsSingleton;
    private final SkillRepository skillRepository;
    private final EnemyFight enemyFight;

    public FightEffectsManagerImplementation(SkillRepository skillRepository, EnemyFight enemyFight) {
        this.fightEffectsSingleton = FightEffectsSingleton.getInstance();
        this.skillRepository = skillRepository;
        this.enemyFight = enemyFight;
    }

    @Override
    public void resetEffects() {
        fightEffectsSingleton.getPlayerStunned().set(false);
        fightEffectsSingleton.getEnemyStunned().set(false);
        fightEffectsSingleton.getPlayerDamageMultiplier().set(1f);
        fightEffectsSingleton.getEnemyDamageMultiplier().set(1f);
        fightEffectsSingleton.getPlayerDefenceMultiplier().set(1f);
        fightEffectsSingleton.getEnemyDefenceMultiplier().set(1f);
    }

    @Override
    public void checkForStunned(FightActionRequestDto request, Character character, FightActionResponse response) {
        var fight = character.getOccupation().getFight();
        if (!fightEffectsSingleton.getPlayerStunned().get()) {
            FightAction action = null;
            switch (request.getAction()) {
                case NORMAL_ATTACK -> action = new NormalAttack();
                case USE_POTION -> action = new UsePotion();
                case SPECIAL_ATTACK -> action = new SpecialAttack(skillRepository);
            }
            if (action != null) {
                action.performAction(character, fight, request, response);
            }
        }
        if (!fightEffectsSingleton.getEnemyStunned().get()) {
            enemyFight.enemyTurn(response, fight, character);
        }
    }
}

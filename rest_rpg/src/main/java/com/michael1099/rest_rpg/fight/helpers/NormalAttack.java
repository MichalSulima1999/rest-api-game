package com.michael1099.rest_rpg.fight.helpers;

import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.fight.model.Fight;
import com.michael1099.rest_rpg.fight.model.FightActionRequestDto;
import org.openapitools.model.FightActionResponse;

import java.util.Random;

// Tydzień 3, Bridge
// Implementacja 1
public class NormalAttack implements FightAction {

    @Override
    public void performAction(Character character, Fight fight, FightActionRequestDto request, FightActionResponse response) {
        var fightEffectsSingleton = FightEffectsSingleton.getInstance();
        var playerStatistics = character.getStatistics();

        var playerDamage = Math.round(character.getStatistics().getDamage() * fightEffectsSingleton.getPlayerDamageMultiplier().get() / fightEffectsSingleton.getEnemyDefenceMultiplier().get());
        if (new Random().nextFloat(0, 100) < playerStatistics.getCriticalChance()) {
            playerDamage *= 2;
            response.setPlayerCriticalStrike(true);
        }
        fight.dealDamageToEnemy(playerDamage);
        response.setPlayerDamage(playerDamage);
        character.setStatistics(playerStatistics);
        character.getOccupation().setFight(fight);
    }
}
// Koniec Tydzień 3, Bridge

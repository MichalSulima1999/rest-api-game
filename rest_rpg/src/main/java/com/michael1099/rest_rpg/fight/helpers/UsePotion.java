package com.michael1099.rest_rpg.fight.helpers;

import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.equipment.Equipment;
import com.michael1099.rest_rpg.exceptions.CharacterHpFullException;
import com.michael1099.rest_rpg.exceptions.NoPotionsLeftException;
import com.michael1099.rest_rpg.fight.model.Fight;
import com.michael1099.rest_rpg.fight.model.FightActionRequestDto;
import com.michael1099.rest_rpg.statistics.Statistics;
import jakarta.validation.constraints.NotNull;
import org.openapitools.model.FightActionResponse;

// Tydzień 3, Bridge
// Implementacja 2
public class UsePotion implements FightAction {

    @Override
    public void performAction(Character character, Fight fight, FightActionRequestDto request, FightActionResponse response) {
        checkIfHpAlreadyFull(character.getStatistics());
        checkIfCharacterHasPotions(character.getEquipment());
        character.usePotion();
    }

    private void checkIfHpAlreadyFull(@NotNull Statistics statistics) {
        if (statistics.getCurrentHp() >= statistics.getMaxHp()) {
            throw new CharacterHpFullException();
        }
    }

    private void checkIfCharacterHasPotions(@NotNull Equipment equipment) {
        if (equipment.getHealthPotions() <= 0) {
            throw new NoPotionsLeftException();
        }
    }
}
// Koniec Tydzień 3, Bridge

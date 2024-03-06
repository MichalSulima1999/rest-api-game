package com.michael1099.rest_rpg.fight.helpers;

import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.fight.model.Fight;
import com.michael1099.rest_rpg.fight.model.FightActionRequestDto;
import org.openapitools.model.FightActionResponse;

// Tydzień 3, Bridge
// Interfejs akcji, abstrakcja
public interface FightAction {

    void performAction(Character character, Fight fight, FightActionRequestDto request, FightActionResponse response);
}
// Koniec Tydzień 3, Bridge

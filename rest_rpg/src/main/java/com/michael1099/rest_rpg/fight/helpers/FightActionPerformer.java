package com.michael1099.rest_rpg.fight.helpers;

import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.fight.model.Fight;
import com.michael1099.rest_rpg.fight.model.FightActionRequestDto;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.FightActionResponse;

// Tydzień 3, Bridge
// Klasa, która zawiera referencję (most) do obiektu FightAction
// Wykorzystywana jest metoda performAction z tego interfejsu,
// co pozwala na oddzielenie implementacji od abstrakcji
@RequiredArgsConstructor
public class FightActionPerformer {

    private final FightAction action;

    public void applyAction(Character character, Fight fight, FightActionRequestDto request, FightActionResponse response) {
        action.performAction(character, fight, request, response);
    }
}
// Koniec Tydzień 3, Bridge

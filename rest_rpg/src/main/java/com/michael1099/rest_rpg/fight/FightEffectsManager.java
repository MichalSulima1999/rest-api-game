package com.michael1099.rest_rpg.fight;

import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.fight.model.FightActionRequestDto;
import org.openapitools.model.FightActionResponse;

public interface FightEffectsManager {

    void resetEffects();

    void checkForStunned(FightActionRequestDto request, Character character, FightActionResponse response);
}

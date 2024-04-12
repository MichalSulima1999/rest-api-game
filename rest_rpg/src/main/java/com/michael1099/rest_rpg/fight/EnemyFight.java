package com.michael1099.rest_rpg.fight;

import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.fight.model.Fight;
import jakarta.validation.constraints.NotNull;
import org.openapitools.model.FightActionResponse;

public interface EnemyFight {

    void enemyTurn(@NotNull FightActionResponse response,
                   @NotNull Fight fight,
                   @NotNull Character character);
}

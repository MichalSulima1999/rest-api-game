package com.michael1099.rest_rpg.fight.command;

import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.exceptions.AdventureNotFoundException;
import com.michael1099.rest_rpg.fight.model.Fight;
import com.michael1099.rest_rpg.fight_effect.CollectionOfFightEffects;
import com.michael1099.rest_rpg.fight_effect.FightEffect;
import com.michael1099.rest_rpg.helpers.iterator.Iterator;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.FightActionResponse;
import org.springframework.context.ApplicationEventPublisher;

@RequiredArgsConstructor
public class EndFight {

    private final Fight fight;
    private final Character character;
    private final FightActionResponse response;
    private final ApplicationEventPublisher eventPublisher;

    public void loseFight() {
        fight.setActive(false);
        fight.setEnemy(null);
        character.getOccupation().setAdventure(null);
        character.getOccupation().setFight(fight);
        if (fight.getFightEffects() != null) {
            fight.getFightEffects().forEach(fightEffect -> fightEffect.setDuration(0));
        }
        response.setPlayerWon(false);
    }

    public void winFight() {
        fight.setActive(false);
        fight.setEnemy(null);
        var adventure = character.getOccupation().getAdventure();
        if (adventure == null) {
            throw new AdventureNotFoundException();
        }
        character.getEquipment().earnGold(adventure.getGoldForAdventure());
        character.earnXp(adventure.getXpForAdventure());
        character.getOccupation().setAdventure(null);
        character.getOccupation().setFight(fight);
        if (fight.getFightEffects() != null) {
            // Tydzień 5, Iterator
            // Stworzona została nowa klasa, która w konstruktorze przyjmuje kolekcję fightEffects
            // Następnie możliwe jest iterowanie po tej kolekcji metodami hasNext i next
            var fightEffects = new CollectionOfFightEffects(fight.getFightEffects());
            for (Iterator iterator = fightEffects.getIterator(); iterator.hasNext(); ) {
                var effect = (FightEffect) iterator.next();
                effect.setDuration(0);
            }
            // Koniec Tydzień 5, Iterator
            //fight.getFightEffects().forEach(fightEffect -> fightEffect.setDuration(0));
        }
        response.setPlayerWon(true);
    }
}

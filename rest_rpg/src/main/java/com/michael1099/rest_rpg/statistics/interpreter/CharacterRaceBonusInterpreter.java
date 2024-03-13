package com.michael1099.rest_rpg.statistics.interpreter;

import com.michael1099.rest_rpg.exceptions.EnumValueNotFoundException;
import com.michael1099.rest_rpg.statistics.Statistics;
import jakarta.validation.constraints.NotNull;
import org.openapitools.model.CharacterRace;
import org.springframework.stereotype.Component;

@Component
public class CharacterRaceBonusInterpreter implements RaceBonusInterpreter {

    public static final int CHARACTER_RACE_BONUS = 5;

    // Tydzień 5, Intepreter
    // Interpreter automatycznie dodaje odpowiednie bonusy rasowe do atrybutów postaci w zależności od jej rasy.
    @Override
    public void setRaceBonus(@NotNull Statistics statistics, @NotNull CharacterRace race) {
        switch (race) {
            case ELF -> statistics.setDexterity(statistics.getDexterity() + CHARACTER_RACE_BONUS);
            case DWARF -> statistics.setConstitution(statistics.getConstitution() + CHARACTER_RACE_BONUS);
            case HUMAN -> statistics.setStrength(statistics.getStrength() + CHARACTER_RACE_BONUS);
            default -> throw new EnumValueNotFoundException();
        }
    }
    // Koniec Tydzień 5, Intepreter
}

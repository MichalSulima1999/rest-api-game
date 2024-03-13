package com.michael1099.rest_rpg.statistics.interpreter;

import com.michael1099.rest_rpg.statistics.Statistics;
import org.openapitools.model.CharacterRace;

public interface RaceBonusInterpreter {

    void setRaceBonus(Statistics statistics, CharacterRace race);
}

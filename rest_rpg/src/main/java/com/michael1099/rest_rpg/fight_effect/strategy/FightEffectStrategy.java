package com.michael1099.rest_rpg.fight_effect.strategy;

import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.fight.model.Fight;
import com.michael1099.rest_rpg.fight_effect.FightEffect;

// Tydzień 6 - Strategy
// Stworzony został interfejs FightEffectStrategy, który jest implementowany przez poszczególne strategie.
// Wszystkie strategie zebrane są w FightEffectStrategyFactory, a następnie stamtąd pobierane w razie potrzeby.
// W tym wypadku strategie pozwoliły na pozbycie się ogromnego switch case w FightService, poprzez pobieranie odpowiednich strategii z mapy, gdzie kluczem jest SkillEffect.
public interface FightEffectStrategy {

    void applyEffect(FightEffect effect, Fight fight, Character character);
}
// Koniec Tydzień 6 - Strategy

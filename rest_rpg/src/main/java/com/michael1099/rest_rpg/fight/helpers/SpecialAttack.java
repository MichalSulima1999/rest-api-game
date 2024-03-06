package com.michael1099.rest_rpg.fight.helpers;

import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.exceptions.NotEnoughManaException;
import com.michael1099.rest_rpg.exceptions.SkillNotFoundException;
import com.michael1099.rest_rpg.fight.model.Fight;
import com.michael1099.rest_rpg.fight.model.FightActionRequestDto;
import com.michael1099.rest_rpg.fight_effect.FightEffect;
import com.michael1099.rest_rpg.skill.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.FightActionResponse;

import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

// Tydzień 3, Bridge
// Implementacja 3
@RequiredArgsConstructor
public class SpecialAttack implements FightAction {

    private final SkillRepository skillRepository;

    @Override
    public void performAction(Character character, Fight fight, FightActionRequestDto request, FightActionResponse response) {
        var fightEffectsSingleton = FightEffectsSingleton.getInstance();
        var skill = skillRepository.get(request.getSkillId());
        var statistics = character.getStatistics();
        var skillLevel = character.getSkills().stream().filter(s -> s.getSkill().getId().equals(skill.getId())).findFirst().orElseThrow(SkillNotFoundException::new).getLevel();
        if (statistics.getCurrentMana() < skill.getManaCost()) {
            throw new NotEnoughManaException();
        }
        if (skill.getEffect() != null) {
            var effects = Optional.ofNullable(fight.getFightEffects()).orElse(new HashSet<>()).stream().
                    filter(fightEffect -> fightEffect.getDuration() <= 0).collect(Collectors.toSet());
            FightEffect fightEffect = new FightEffect();
            if (!effects.isEmpty()) {
                fightEffect = effects.stream().findFirst().get();
            }
            fightEffect.setFight(fight);
            int effectDuration = skill.getFinalEffectDuration(skillLevel);
            fightEffect.setDuration(effectDuration);
            fightEffect.setPlayerEffect(false);
            fightEffect.setSkillEffect(skill.getEffect());
            fightEffect.setEffectMultiplier(skill.getFinalEffectMultiplier(skillLevel));
            fight.addFightEffect(fightEffect);
        }
        var baseDamage = skill.isMagicDamage() ? statistics.getMagicDamage() : statistics.getDamage();
        var playerDamage = Math.round(skill.getDamageMultiplier(skillLevel) * baseDamage / fightEffectsSingleton.getEnemyDefenceMultiplier().get());
        statistics.useMana(skill.getManaCost());
        fight.dealDamageToEnemy(playerDamage);
        response.setPlayerDamage(playerDamage);
        response.setPlayerCurrentMana(statistics.getCurrentMana());
        character.setStatistics(statistics);
    }
}
// Koniec Tydzień 3, Bridge

package com.michael1099.rest_rpg.character.mediator;

import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.character_skill.CharacterSkill;
import com.michael1099.rest_rpg.equipment.Equipment;
import com.michael1099.rest_rpg.skill.model.Skill;
import com.michael1099.rest_rpg.statistics.Statistics;
import lombok.RequiredArgsConstructor;

import java.util.Set;

// Tydzień 5, Mediator
// Ten mediator pomaga podczas nauki nowej umiejętności, co wymaga użycia wielu klas.
// Dzięki temu komunikacja między komponentami jest zarządzana przez mediatora
@RequiredArgsConstructor
public class LearnSkillMediator {

    private final Equipment equipment;
    private final Character character;
    private final Statistics statistics;
    private final Set<CharacterSkill> skills;

    public void learnNewSkill(Skill skill) {
        var currentSkill = skills.stream().filter(s -> s.getSkill().getId().equals(skill.getId())).findFirst();
        currentSkill.ifPresentOrElse(CharacterSkill::upgrade, () -> {
            var characterSkill = CharacterSkill.newSkill(skill, character);
            skills.add(characterSkill);
        });
        equipment.spendGold(skill.getSkillTraining().getGoldCost());
        statistics.useStatisticPoints(skill.getSkillTraining().getStatisticPointsCost());
    }
}
// Koniec Tydzień 5, Mediator

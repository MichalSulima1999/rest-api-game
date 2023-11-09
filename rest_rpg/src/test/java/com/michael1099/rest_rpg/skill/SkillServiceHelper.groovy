package com.michael1099.rest_rpg.skill

import com.michael1099.rest_rpg.character_skill.CharacterSkill
import com.michael1099.rest_rpg.skill.model.Skill
import org.openapitools.model.CharacterClass
import org.openapitools.model.SkillEffect
import org.openapitools.model.SkillType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SkillServiceHelper {

    @Autowired
    SkillRepository skillRepository

    def clean() {
        skillRepository.deleteAll()
    }

    Skill createSkill(Map customArgs = [:]) {
        Map args = [
                name                    : "John" + Math.random().toString(),
                type                    : SkillType.NORMAL_DAMAGE,
                multiplier              : 1.3,
                multiplierPerLevel      : 0.2,
                effect                  : SkillEffect.BLEEDING,
                effectDuration          : 3,
                effectDurationPerLevel  : 1,
                effectMultiplier        : 0.1,
                effectMultiplierPerLevel: 0.05,
                manaCost                : 10,
                enemy                   : null,
                characterClass          : CharacterClass.WARRIOR,
                characterSkills         : new HashSet<CharacterSkill>(),
                deleted                 : false
        ]
        args << customArgs

        def skill = Skill.builder()
                .name(args.name)
                .type(args.type)
                .multiplier(args.multiplier)
                .effect(args.effect)
                .effectDuration(args.effectDuration)
                .effectDurationPerLevel(args.effectDurationPerLevel)
                .effectMultiplier(args.effectMultiplier)
                .effectMultiplierPerLevel(args.effectMultiplierPerLevel)
                .manaCost(args.manaCost)
                .enemy(args.enemy)
                .characterClass(args.characterClass)
                .characters(args.characterSkills)
                .deleted(args.deleted)
                .build()

        return skillRepository.save(skill)
    }
}

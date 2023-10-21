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
                name           : "John" + Math.random().toString(),
                type           : SkillType.NORMAL_DAMAGE,
                multiplier     : 2,
                effect         : SkillEffect.BLEEDING,
                enemy          : null,
                characterClass : CharacterClass.WARRIOR,
                characterSkills: new HashSet<CharacterSkill>()
        ]
        args << customArgs

        def skill = Skill.builder()
                .name(args.name)
                .type(args.type)
                .multiplier(args.multiplier)
                .effect(args.effect)
                .enemy(args.enemy)
                .characterClass(args.characterClass)
                .characters(args.characterSkills)
                .build()

        return skillRepository.save(skill)
    }
}

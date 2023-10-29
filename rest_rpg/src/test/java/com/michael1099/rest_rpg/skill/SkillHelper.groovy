package com.michael1099.rest_rpg.skill

import com.michael1099.rest_rpg.helpers.PageHelper
import com.michael1099.rest_rpg.skill.model.Skill
import org.openapitools.model.CharacterClass
import org.openapitools.model.SkillBasic
import org.openapitools.model.SkillBasicPage
import org.openapitools.model.SkillCreateRequest
import org.openapitools.model.SkillDetails
import org.openapitools.model.SkillEffect
import org.openapitools.model.SkillLite
import org.openapitools.model.SkillLites
import org.openapitools.model.SkillSearchRequest
import org.openapitools.model.SkillType

class SkillHelper {

    static SkillCreateRequest createSkillCreateRequest(Map customArgs = [:]) {
        Map args = [
                name            : "Bash",
                manaCost        : 10,
                type            : SkillType.NORMAL_DAMAGE.toString(),
                multiplier      : 1.2f,
                effect          : SkillEffect.STUNNED.toString(),
                effectDuration  : 2,
                effectMultiplier: null,
                characterClass  : CharacterClass.WARRIOR.toString()
        ]

        args << customArgs
        def request = new SkillCreateRequest(args.name, args.manaCost, args.type, args.multiplier, args.characterClass)
        request.multiplierPerLevel(args.multiplierPerLevel as Float)
        request.effect(args.effect)
        request.effectDuration(args.effectDuration)
        request.effectDurationPerLevel(args.effectDurationPerLevel as Integer)
        request.effectMultiplier(args.effectMultiplier)
        request.effectMultiplierPerLevel(args.effectMultiplierPerLevel as Float)
        return request
    }

    static SkillSearchRequest createSkillSearchRequest(Map args = [:]) {
        new SkillSearchRequest()
                .pagination(PageHelper.createPagination(args))
                .nameLike(args.name as String)
                .skillTypeIn(args.skillTypeIn as List)
                .skillEffectIn(args.skillEffectIn as List)
                .characterClassIn(args.characterClassIn as List)
    }

    static boolean compare(SkillCreateRequest request, SkillLite skillLite) {
        assert request.name == skillLite.name

        true
    }

    static boolean compare(Skill skill, SkillLite skillLite) {
        assert skill.id == skillLite.id
        assert skill.name == skillLite.name

        true
    }

    static boolean compare(List<Skill> skills, SkillLites skillLites) {
        def skillLiteList = skillLites.content
        assert skills.size() == skillLiteList.size()
        skills = skills.sort { it.id }
        skillLiteList = skillLiteList.sort { it.id }
        assert skills.withIndex().every { compare(it.v1, skillLiteList[it.v2]) }

        true
    }

    static boolean compare(Skill skill, SkillBasic skillBasic) {
        assert skill.id == skillBasic.id
        assert skill.name == skillBasic.name
        assert skill.type.toString() == skillBasic.type
        assert skill.effect.toString() == skillBasic.effect
        assert skill.characterClass.toString() == skillBasic.characterClass

        true
    }

    static boolean compare(List<Skill> skills, SkillBasicPage skillBasicPage) {
        def skillBasics = skillBasicPage.content
        assert skills.size() == skillBasics.size()
        skills = skills.sort { it.id }
        skillBasics = skillBasics.sort { it.id }
        assert skills.withIndex().every { compare(it.v1, skillBasics[it.v2]) }

        true
    }

    static boolean compare(Skill skill, SkillDetails skillDetails) {
        assert skill.id == skillDetails.id
        assert skill.name == skillDetails.name
        assert skill.manaCost == skillDetails.manaCost
        assert skill.type.toString() == skillDetails.type
        assert skill.effect.toString() == skillDetails.effect
        assert skill.characterClass.toString() == skillDetails.characterClass
        assert skill.multiplier == skillDetails.multiplier
        assert skill.multiplierPerLevel == skillDetails.multiplierPerLevel
        assert skill.effectDuration == skillDetails.effectDuration
        assert skill.effectMultiplier == skillDetails.effectMultiplier
        assert skill.effectDurationPerLevel == skillDetails.effectDurationPerLevel
        assert skill.effectMultiplierPerLevel == skillDetails.effectMultiplierPerLevel

        true
    }
}

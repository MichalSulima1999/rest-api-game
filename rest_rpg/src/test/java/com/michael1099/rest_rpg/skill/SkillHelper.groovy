package com.michael1099.rest_rpg.skill

import org.openapitools.model.CharacterClass
import org.openapitools.model.SkillCreateRequest
import org.openapitools.model.SkillEffect
import org.openapitools.model.SkillLite
import org.openapitools.model.SkillType

class SkillHelper {

    static SkillCreateRequest createSkillCreateRequest(Map customArgs = [:]) {
        Map args = [
                name            : "Bash",
                type            : SkillType.NORMAL_DAMAGE.toString(),
                multiplier      : 1.2f,
                effect          : SkillEffect.STUNNED.toString(),
                effectDuration  : 2,
                effectMultiplier: null,
                characterClass  : CharacterClass.WARRIOR.toString()
        ]

        args << customArgs
        def request = new SkillCreateRequest(args.name, args.type, args.multiplier, args.characterClass)
        request.effect(args.effect)
        request.effectDuration(args.effectDuration)
        request.effectMultiplier(args.effectMultiplier)
        return request
    }

    static boolean compare(SkillCreateRequest request, SkillLite skillLite) {
        assert request.name == skillLite.name
        assert request.type == skillLite.type
        assert request.multiplier == skillLite.multiplier
        assert request.effect == skillLite.effect
        assert request.effectDuration == skillLite.effectDuration
        assert request.effectMultiplier == skillLite.effectMultiplier
        assert request.characterClass == skillLite.characterClass

        true
    }
}

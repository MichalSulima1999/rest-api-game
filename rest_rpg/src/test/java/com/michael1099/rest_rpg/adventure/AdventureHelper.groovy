package com.michael1099.rest_rpg.adventure

import com.michael1099.rest_rpg.enemy.model.Enemy
import com.michael1099.rest_rpg.helpers.PageHelper
import org.openapitools.model.AdventureCreateRequest
import org.openapitools.model.AdventureLite
import org.openapitools.model.AdventureSearchRequest

class AdventureHelper {

    static AdventureCreateRequest createAdventureCreateRequest(Enemy enemy, Map customArgs = [:]) {
        Map args = [
                name                    : "Kill bear",
                adventureLengthInMinutes: 90,
                xpForAdventure          : 100,
                goldForAdventure        : 110
        ]

        args << customArgs

        return new AdventureCreateRequest(args.name, args.adventureLengthInMinutes, args.xpForAdventure, args.goldForAdventure, enemy.id)
    }

    static AdventureSearchRequest createAdventureSearchRequest(Map args = [:]) {
        new AdventureSearchRequest() // TODO
                .pagination(PageHelper.createPagination(args))
                .nameLike(args.name as String)
                .skillTypeIn(args.skillTypeIn as List)
                .skillEffectIn(args.skillEffectIn as List)
                .characterClassIn(args.characterClassIn as List)
    }

    static boolean compare(AdventureCreateRequest request, AdventureLite lite) {
        assert request.name == lite.name

        true
    }
}

package com.michael1099.rest_rpg.character

import com.michael1099.rest_rpg.character.model.CharacterArtwork
import com.michael1099.rest_rpg.character.model.CharacterClass
import com.michael1099.rest_rpg.character.model.CharacterRace
import com.michael1099.rest_rpg.character.model.CharacterSex
import com.michael1099.rest_rpg.statistics.StatisticsHelper
import org.openapitools.model.CharacterCreateRequest
import org.openapitools.model.CharacterLite

class CharacterHelper {

    static CharacterCreateRequest createCharacterCreateRequest(Map customArgs = [:]) {
        Map args = [
                name          : "John" + Math.random().toString(),
                race          : CharacterRace.HUMAN.toString(),
                sex           : CharacterSex.FEMALE.toString(),
                characterClass: CharacterClass.WARRIOR.toString(),
                artwork       : CharacterArtwork.HUMAN_FEMALE_1.toString(),
                statistics    : StatisticsHelper.createStatisticsUpdateRequest()
        ]

        args << customArgs

        return new CharacterCreateRequest(args.name, args.race, args.sex, args.characterClass, args.artwork, args.statistics)
    }

    static boolean compare(CharacterCreateRequest request, CharacterLite characterLite) {
        assert request.sex == characterLite.sex
        assert request.artwork == characterLite.artwork
        assert request.characterClass == characterLite.characterClass
        assert request.race == characterLite.race
        assert request.name == characterLite.name

        true
    }
}

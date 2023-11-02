package com.michael1099.rest_rpg.character

import com.michael1099.rest_rpg.auth.user.User
import com.michael1099.rest_rpg.character.model.Character
import com.michael1099.rest_rpg.character.model.CharacterArtwork
import com.michael1099.rest_rpg.equipment.Equipment
import com.michael1099.rest_rpg.equipment.EquipmentRepository
import com.michael1099.rest_rpg.fight.Fight
import com.michael1099.rest_rpg.fight.FightRepository
import com.michael1099.rest_rpg.occupation.Occupation
import com.michael1099.rest_rpg.occupation.OccupationRepository
import com.michael1099.rest_rpg.skill.SkillRepository
import com.michael1099.rest_rpg.statistics.StatisticsHelper
import com.michael1099.rest_rpg.statistics.StatisticsRepository
import org.openapitools.model.CharacterClass
import org.openapitools.model.CharacterRace
import org.openapitools.model.CharacterSex
import org.openapitools.model.CharacterStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CharacterServiceHelper {

    @Autowired
    CharacterRepository characterRepository

    @Autowired
    SkillRepository skillRepository

    @Autowired
    StatisticsRepository statisticsRepository

    @Autowired
    OccupationRepository occupationRepository

    @Autowired
    FightRepository fightRepository

    @Autowired
    EquipmentRepository equipmentRepository

    def clean() {
        characterRepository.deleteAll()
        skillRepository.deleteAll()
        statisticsRepository.deleteAll()
        occupationRepository.deleteAll()
        fightRepository.deleteAll()
        equipmentRepository.deleteAll()
    }

    Character createCharacter(User user, Map customArgs = [:]) {
        Map args = [
                name          : "John" + Math.random().toString(),
                race          : CharacterRace.HUMAN,
                sex           : CharacterSex.FEMALE,
                characterClass: CharacterClass.WARRIOR,
                status        : CharacterStatus.IDLE,
                artwork       : CharacterArtwork.HUMAN_FEMALE_1,
                skills        : [],
                statistics    : StatisticsHelper.statistics(customArgs),
                occupation    : Occupation.builder().fight(new Fight()).build(),
                equipment     : Equipment.init()
        ]
        args << customArgs

        def character = Character.builder()
                .name(args.name)
                .characterClass(args.characterClass)
                .artwork(args.artwork)
                .race(args.race)
                .sex(args.sex)
                .equipment(args.equipment)
                .occupation(args.occupation)
                .statistics(args.statistics)
                .status(args.status)
                .user(user)
                .build()

        character.occupation.setCharacter(character)
        character.statistics.setCharacter(character)
        character.getOccupation().getFight().setOccupation(character.occupation)
        character.equipment.setCharacter(character)

        save(character)
    }

    Character save(Character character) {
        characterRepository.save(character)
    }

    Character getCharacter(long characterId) {
        return characterRepository.getCharacterWithTestGraphById(characterId)
    }
}

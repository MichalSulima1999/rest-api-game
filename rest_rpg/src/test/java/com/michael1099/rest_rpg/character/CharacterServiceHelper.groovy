package com.michael1099.rest_rpg.character

import com.michael1099.rest_rpg.auth.user.User
import com.michael1099.rest_rpg.character.model.Character
import com.michael1099.rest_rpg.character.model.CharacterArtwork
import com.michael1099.rest_rpg.character_skill.CharacterSkill
import com.michael1099.rest_rpg.enemy.EnemyRepository
import com.michael1099.rest_rpg.equipment.Equipment
import com.michael1099.rest_rpg.equipment.EquipmentRepository
import com.michael1099.rest_rpg.fight.FightRepository
import com.michael1099.rest_rpg.fight.model.Fight
import com.michael1099.rest_rpg.occupation.Occupation
import com.michael1099.rest_rpg.occupation.OccupationRepository
import com.michael1099.rest_rpg.skill.SkillRepository
import com.michael1099.rest_rpg.skill.model.Skill
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
    EnemyRepository enemyRepository

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
        enemyRepository.deleteAll()
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
                skills        : new HashSet<Skill>(),
                statistics    : StatisticsHelper.statistics(),
                occupation    : Occupation.builder().fight(new Fight()).build(),
                equipment     : Equipment.builder().gold(1000).build()
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
                .skills(new HashSet<CharacterSkill>())
                .status(args.status)
                .user(user)
                .build()

        character.occupation.setCharacter(character)
        character.statistics.setCharacter(character)
        character.getOccupation().getFight().setOccupation(character.occupation)
        character.equipment.setCharacter(character)

        character = save(character)

        args.skills.forEach {
            character.learnNewSkill(it)
        }

        save(character)
    }

    Character save(Character character) {
        characterRepository.save(character)
    }

    Character getCharacter(long characterId) {
        return characterRepository.getCharacterWithTestGraphById(characterId)
    }
}

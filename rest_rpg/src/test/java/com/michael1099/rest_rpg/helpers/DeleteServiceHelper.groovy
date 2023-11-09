package com.michael1099.rest_rpg.helpers

import com.michael1099.rest_rpg.character.CharacterRepository
import com.michael1099.rest_rpg.enemy.EnemyRepository
import com.michael1099.rest_rpg.equipment.EquipmentRepository
import com.michael1099.rest_rpg.fight.FightRepository
import com.michael1099.rest_rpg.fight_effect.FightEffectRepository
import com.michael1099.rest_rpg.item.ItemRepository
import com.michael1099.rest_rpg.occupation.OccupationRepository
import com.michael1099.rest_rpg.skill.SkillRepository
import com.michael1099.rest_rpg.statistics.StatisticsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DeleteServiceHelper {

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

    @Autowired
    FightEffectRepository fightEffectRepository

    @Autowired
    ItemRepository itemRepository

    def clean() {
        characterRepository.deleteAll()
        enemyRepository.deleteAll()
        skillRepository.deleteAll()
        statisticsRepository.deleteAll()
        occupationRepository.deleteAll()
        fightRepository.deleteAll()
        equipmentRepository.deleteAll()
        fightEffectRepository.deleteAll()
        itemRepository.deleteAll()
    }
}

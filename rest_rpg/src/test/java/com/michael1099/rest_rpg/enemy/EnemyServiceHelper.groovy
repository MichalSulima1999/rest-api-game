package com.michael1099.rest_rpg.enemy

import com.michael1099.rest_rpg.enemy.model.Enemy
import com.michael1099.rest_rpg.skill.SkillServiceHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.time.LocalDateTime

@Service
class EnemyServiceHelper {

    @Autowired
    SkillServiceHelper skillServiceHelper

    @Autowired
    EnemyRepository enemyRepository

    def clean() {
        enemyRepository.deleteAll()
        skillServiceHelper.clean()
    }

    Enemy saveEnemy(Map customArgs = [:]) {
        Map args = [
                name            : "Bear" + LocalDateTime.now().getNano(),
                hp              : 200,
                mana            : 200,
                damage          : 20,
                numberOfPotions : 2,
                skill           : skillServiceHelper.createSkill(),
                skillLevel      : 1,
                strategyElements: [EnemyHelper.createStrategyElement()],
                deleted         : false
        ]

        args << customArgs
        def enemy = Enemy.builder()
                .name(args.name)
                .hp(args.hp)
                .mana(args.mana)
                .damage(args.damage)
                .numberOfPotions(args.numberOfPotions)
                .skill(args.skill)
                .skillLevel(args.skillLevel)
                .strategyElements(args.strategyElements as Set)
                .deleted(args.deleted)
                .build()
        return enemyRepository.save(enemy)
    }
}

package com.michael1099.rest_rpg.adventure

import com.michael1099.rest_rpg.adventure.model.Adventure
import com.michael1099.rest_rpg.enemy.EnemyServiceHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AdventureServiceHelper {

    @Autowired
    AdventureRepository adventureRepository

    @Autowired
    EnemyServiceHelper enemyServiceHelper

    def clean() {
        adventureRepository.deleteAll()
        enemyServiceHelper.clean()
    }

    Adventure saveAdventure(Map customArgs = [:]) {
        Map args = [
                name                    : "Kill bear",
                adventureLengthInMinutes: 90,
                xpForAdventure          : 100,
                goldForAdventure        : 110,
                enemy                   : enemyServiceHelper.saveEnemy(customArgs.enemy as Map)
        ]
        args << customArgs

        def adventure = Adventure.builder()
                .name(args.name)
                .adventureTimeInMinutes(args.adventureLengthInMinutes)
                .xpForAdventure(args.xpForAdventure)
                .goldForAdventure(args.goldForAdventure)
                .enemy(args.enemy)
                .build()

        adventureRepository.save(adventure)
    }
}

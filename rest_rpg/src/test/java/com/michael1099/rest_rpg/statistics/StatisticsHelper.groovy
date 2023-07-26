package com.michael1099.rest_rpg.statistics

import org.openapitools.model.StatisticsUpdateRequest

class StatisticsHelper {

    static StatisticsUpdateRequest createStatisticsUpdateRequest(Map customArgs = [:]) {
        Map args = [
                strength    : 2,
                dexterity   : 2,
                constitution: 3,
                intelligence: 3
        ]
        args << customArgs
        def request = new StatisticsUpdateRequest()
        request.strength(args.strength)
        request.dexterity(args.dexterity)
        request.constitution(args.constitution)
        request.intelligence(args.intelligence)

        return request
    }

    static Statistics statistics(Map customArgs = [:]) {
        Map args = [
                maxHp              : 300,
                currentHp          : 300,
                maxMana            : 100,
                currentMana        : 100,
                damage             : 40,
                magicDamage        : 40,
                armor              : 20,
                dodgeChance        : 40.1,
                criticalChance     : 50.6,
                currentXp          : 100,
                xpToNextLevel      : 250,
                currentLevel       : 2,
                freeStatisticPoints: 10,
                strength           : 40,
                dexterity          : 30,
                constitution       : 30,
                intelligence       : 30,
                character          : null,
                deleted            : false
        ]
        args << customArgs

        return Statistics.builder()
                .maxHp(args.maxHp)
                .currentHp(args.currentHp)
                .maxMana(args.maxMana)
                .currentMana(args.currentMana)
                .damage(args.damage)
                .magicDamage(args.magicDamage)
                .armor(args.armor)
                .dodgeChance(args.dodgeChance)
                .criticalChance(args.criticalChance)
                .currentXp(args.currentXp)
                .xpToNextLevel(args.xpToNextLevel)
                .currentLevel(args.currentLevel)
                .freeStatisticPoints(args.freeStatisticPoints)
                .strength(args.strength)
                .dexterity(args.dexterity)
                .constitution(args.constitution)
                .intelligence(args.intelligence)
                .character(args.character)
                .deleted(args.deleted)
                .build()
    }
}

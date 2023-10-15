package com.michael1099.rest_rpg.enemy

import com.michael1099.rest_rpg.skill.Skill
import org.openapitools.model.*

class EnemyHelper {

    static EnemyCreateRequest createEnemyCreateRequest(Skill skill, Map customArgs = [:]) {
        Map args = [
                name           : "Bear",
                hp             : 200,
                mana           : 200,
                damage         : 20,
                numberOfPotions: 2,
                skillLevel     : 1,
                enemyStrategy  : [createStrategyElementCreateRequest()]
        ]

        args << customArgs

        return new EnemyCreateRequest(args.name, args.hp, args.mana, args.damage, args.numberOfPotions,
                skill.id, args.skillLevel, args.enemyStrategy)
    }

    static StrategyElementCreateRequest createStrategyElementCreateRequest(Map customArgs = [:]) {
        Map args = [
                event   : ElementEvent.ENEMY_HEALTH_20_40,
                action  : ElementAction.NORMAL_ATTACK,
                priority: 1
        ]
        args << customArgs

        return new StrategyElementCreateRequest(args.event, args.action, args.priority)
    }

    static boolean compare(EnemyCreateRequest request, EnemyLite enemyLite) {
        assert request.name == enemyLite.name
        assert request.damage == enemyLite.damage
        assert request.hp == enemyLite.hp
        assert request.mana == enemyLite.mana
        assert request.numberOfPotions == enemyLite.numberOfPotions

        true
    }
}

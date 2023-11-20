package com.michael1099.rest_rpg.fight

import com.michael1099.rest_rpg.enemy.EnemyHelper
import com.michael1099.rest_rpg.fight.model.Fight
import com.michael1099.rest_rpg.fight_effect.FightEffect
import org.openapitools.model.FightDetails
import org.openapitools.model.FightEffectLite

class FightHelper {

    static boolean compare(Fight fight, FightDetails details) {
        assert fight.id == details.id
        assert fight.enemyCurrentHp == details.enemyCurrentHp
        assert fight.enemyCurrentMana == details.enemyCurrentMana
        assert fight.active == details.active
        fight.enemy == null || EnemyHelper.compare(fight.enemy, details.enemy)
        compare(fight.fightEffects, details.fightEffects)

        true
    }

    static boolean compare(FightEffect effect, FightEffectLite lite) {
        assert effect.id == lite.id
        assert effect.skillEffect.toString() == lite.skillEffect
        assert effect.duration == lite.duration
        assert effect.playerEffect == lite.playerEffect

        true
    }

    static boolean compare(Collection<FightEffect> effects, List<FightEffectLite> lites) {
        assert effects.size() == lites.size()
        effects = effects.sort { it.id }
        lites = lites.sort { it.id }
        assert effects.withIndex().every { compare(it.v1, lites[it.v2]) }

        true
    }
}

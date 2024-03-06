package com.michael1099.rest_rpg.fight.helpers;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

// Tydzień 2, Wzorzec Singleton
// Klasyczny Singleton
@Getter
public final class FightEffectsSingleton {

    private static final FightEffectsSingleton INSTANCE = new FightEffectsSingleton();

    private final AtomicBoolean playerStunned = new AtomicBoolean(false);
    private final AtomicBoolean enemyStunned = new AtomicBoolean(false);
    private final AtomicReference<Float> playerDamageMultiplier = new AtomicReference<>((float) 1);
    private final AtomicReference<Float> enemyDamageMultiplier = new AtomicReference<>((float) 1);
    private final AtomicReference<Float> playerDefenceMultiplier = new AtomicReference<>((float) 1);
    private final AtomicReference<Float> enemyDefenceMultiplier = new AtomicReference<>((float) 1);

    public static FightEffectsSingleton getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final FightEffectsSingleton INSTANCE = new FightEffectsSingleton();
    }
}
// Koniec Tydzień 2, Wzorzec Singleton

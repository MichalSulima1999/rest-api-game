package com.michael1099.rest_rpg.fight;

import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.enemy.model.StrategyElement;
import com.michael1099.rest_rpg.fight.model.Fight;
import jakarta.validation.constraints.NotNull;
import org.openapitools.model.ElementEvent;
import org.openapitools.model.FightActionResponse;

import java.util.Optional;
import java.util.Set;

public class EnemyFightImplementation implements EnemyFight {

    // Tydzień 9 - wyeliminuj magiczne liczby
    // W metodzie selectAction były magiczne liczby, które teraz zostały umieszczone poniżej w stałych
    private static final int HEALTH_THRESHOLD_20 = 20;
    private static final int HEALTH_THRESHOLD_40 = 40;
    private static final int HEALTH_THRESHOLD_60 = 60;
    private static final int HEALTH_THRESHOLD_80 = 80;
    // Koniec Tydzień 9 - wyeliminuj magiczne liczby

    @Override
    public void enemyTurn(@NotNull FightActionResponse response,
                          @NotNull Fight fight,
                          @NotNull Character character) {
        var enemy = Optional.ofNullable(fight.getEnemy()).orElseThrow();
        var playerStatistics = character.getStatistics();
        if (fight.getEnemyCurrentHp() > 0) {
            var enemyAction = decideEnemyAction(fight, character);
            response.setEnemyAction(enemyAction.getElementAction());
            // Tydzień 2, Wzorzec Factory
            // Tworzone są obiekty na podstawie wartości pola elementAction
            // Klasy są tworzone na podstawie interfejsu, a następnie wywoływana jest metoda perform()
            EnemyAction enemyAction1 = null;
            switch (enemyAction.getElementAction()) {
                case NORMAL_ATTACK -> enemyAction1 = new EnemyNormalAttack(response, playerStatistics, enemy);
                case SPECIAL_ATTACK -> enemyAction1 = new EnemySpecialAttack(response, playerStatistics, enemy, fight);
                case USE_POTION -> enemyAction1 = new EnemyUsePotion(response, playerStatistics, enemy, fight);
            }
            enemyAction1.perform();
            // Koniec Tydzień 2, Wzorzec Factory
        }
        character.setStatistics(playerStatistics);
        character.getOccupation().setFight(fight);
    }

    private StrategyElement decideEnemyAction(@NotNull Fight fight, @NotNull Character character) {
        var enemy = Optional.ofNullable(fight.getEnemy()).orElseThrow();
        var strategy = enemy.getStrategyElements();
        var enemyHpPercent = calculateHpPercent(fight.getEnemyCurrentHp(), enemy.getHp());
        var playerHpPercent = calculateHpPercent(character.getStatistics().getCurrentHp(), character.getStatistics().getMaxHp());
        return selectAction(strategy, enemyHpPercent, playerHpPercent);
    }

    private float calculateHpPercent(int currentHp, int maxHp) {
        return ((float) currentHp / (float) maxHp) * 100;
    }

    // Tydzień 9 - dostosuj długości metod w programie, żeby nie miały więcej niż 20 linii
    // Ta metoda była bardzo długa, ale udało się ją podzielić na więcej metod
    private StrategyElement selectAction(Set<StrategyElement> strategy, float enemyHpPercent, float playerHpPercent) {
        if (enemyHpPercent < HEALTH_THRESHOLD_20) {
            return selectByElementEvent(strategy, ElementEvent.ENEMY_HEALTH_0_20);
        } else if (playerHpPercent < HEALTH_THRESHOLD_20) {
            return selectByElementEvent(strategy, ElementEvent.PLAYER_HEALTH_0_20);
        } else if (enemyHpPercent < HEALTH_THRESHOLD_40) {
            return selectByElementEvent(strategy, ElementEvent.ENEMY_HEALTH_20_40);
        } else if (playerHpPercent < HEALTH_THRESHOLD_40) {
            return selectByElementEvent(strategy, ElementEvent.PLAYER_HEALTH_20_40);
        } else if (enemyHpPercent < HEALTH_THRESHOLD_60) {
            return selectByElementEvent(strategy, ElementEvent.ENEMY_HEALTH_40_60);
        } else if (playerHpPercent < HEALTH_THRESHOLD_60) {
            return selectByElementEvent(strategy, ElementEvent.PLAYER_HEALTH_40_60);
        } else if (enemyHpPercent < HEALTH_THRESHOLD_80) {
            return selectByElementEvent(strategy, ElementEvent.ENEMY_HEALTH_60_80);
        } else if (playerHpPercent < HEALTH_THRESHOLD_80) {
            return selectByElementEvent(strategy, ElementEvent.PLAYER_HEALTH_60_80);
        } else {
            StrategyElement playerAction = selectByElementEvent(strategy, ElementEvent.PLAYER_HEALTH_80_100);
            StrategyElement enemyAction = selectByElementEvent(strategy, ElementEvent.ENEMY_HEALTH_80_100);
            return playerAction.getPriority() > enemyAction.getPriority() ? playerAction : enemyAction;
        }
    }
    // Koniec Tydzień 9 - dostosuj długości metod w programie, żeby nie miały więcej niż 20 linii

    private StrategyElement selectByElementEvent(Set<StrategyElement> strategy, ElementEvent elementEvent) {
        return strategy.stream()
                .filter(s -> s.getElementEvent() == elementEvent)
                .findFirst()
                .orElseThrow();
    }
}

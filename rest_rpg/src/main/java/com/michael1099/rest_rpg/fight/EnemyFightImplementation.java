package com.michael1099.rest_rpg.fight;

import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.enemy.model.StrategyElement;
import com.michael1099.rest_rpg.fight.model.Fight;
import jakarta.validation.constraints.NotNull;
import org.openapitools.model.ElementEvent;
import org.openapitools.model.FightActionResponse;

import java.util.Optional;

public class EnemyFightImplementation implements EnemyFight {

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
        StrategyElement enemyAction;
        var strategy = enemy.getStrategyElements();
        var enemyHpPercent = (float) fight.getEnemyCurrentHp() / (float) enemy.getHp() * 100;
        var playerHpPercent = (float) character.getStatistics().getCurrentHp() / (float) character.getStatistics().getMaxHp() * 100;
        if (enemyHpPercent < 20) {
            enemyAction = strategy.stream().filter(s ->
                    s.getElementEvent() == ElementEvent.ENEMY_HEALTH_0_20).findFirst().orElseThrow();
        } else if (playerHpPercent < 20) {
            enemyAction = strategy.stream().filter(s ->
                    s.getElementEvent() == ElementEvent.PLAYER_HEALTH_0_20).findFirst().orElseThrow();
        } else if (enemyHpPercent < 40) {
            enemyAction = strategy.stream().filter(s ->
                    s.getElementEvent() == ElementEvent.ENEMY_HEALTH_20_40).findFirst().orElseThrow();
        } else if (playerHpPercent < 40) {
            enemyAction = strategy.stream().filter(s ->
                    s.getElementEvent() == ElementEvent.PLAYER_HEALTH_20_40).findFirst().orElseThrow();
        } else if (enemyHpPercent < 60) {
            enemyAction = strategy.stream().filter(s ->
                    s.getElementEvent() == ElementEvent.ENEMY_HEALTH_40_60).findFirst().orElseThrow();
        } else if (playerHpPercent < 60) {
            enemyAction = strategy.stream().filter(s ->
                    s.getElementEvent() == ElementEvent.PLAYER_HEALTH_40_60).findFirst().orElseThrow();
        } else if (enemyHpPercent < 80) {
            enemyAction = strategy.stream().filter(s ->
                    s.getElementEvent() == ElementEvent.ENEMY_HEALTH_60_80).findFirst().orElseThrow();
        } else if (playerHpPercent < 80) {
            enemyAction = strategy.stream().filter(s ->
                    s.getElementEvent() == ElementEvent.PLAYER_HEALTH_60_80).findFirst().orElseThrow();
        } else {
            var action = strategy.stream().filter(s ->
                    s.getElementEvent() == ElementEvent.PLAYER_HEALTH_80_100).findFirst().orElseThrow();
            var action2 = strategy.stream().filter(s ->
                    s.getElementEvent() == ElementEvent.ENEMY_HEALTH_80_100).findFirst().orElseThrow();
            enemyAction = action.getPriority() > action2.getPriority() ? action : action2;
        }
        return enemyAction;
    }
}

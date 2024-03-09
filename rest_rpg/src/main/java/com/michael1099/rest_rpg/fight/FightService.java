package com.michael1099.rest_rpg.fight;

import com.michael1099.rest_rpg.auth.auth.IAuthenticationFacade;
import com.michael1099.rest_rpg.character.CharacterRepository;
import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.enemy.model.Enemy;
import com.michael1099.rest_rpg.enemy.model.StrategyElement;
import com.michael1099.rest_rpg.exceptions.AdventureNotFoundException;
import com.michael1099.rest_rpg.exceptions.FightIsNotActiveException;
import com.michael1099.rest_rpg.fight.helpers.FightAction;
import com.michael1099.rest_rpg.fight.helpers.FightEffectsSingleton;
import com.michael1099.rest_rpg.fight.helpers.NormalAttack;
import com.michael1099.rest_rpg.fight.helpers.SpecialAttack;
import com.michael1099.rest_rpg.fight.helpers.UsePotion;
import com.michael1099.rest_rpg.fight.model.Fight;
import com.michael1099.rest_rpg.fight_effect.CollectionOfFightEffects;
import com.michael1099.rest_rpg.fight_effect.FightEffect;
import com.michael1099.rest_rpg.helpers.iterator.Iterator;
import com.michael1099.rest_rpg.skill.SkillRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.openapitools.model.ElementEvent;
import org.openapitools.model.FightActionRequest;
import org.openapitools.model.FightActionResponse;
import org.openapitools.model.FightDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Validated
public class FightService {

    public static int MANA_REGENERATION_PERCENT_PER_TURN = 10;

    private final CharacterRepository characterRepository;
    private final SkillRepository skillRepository;
    private final IAuthenticationFacade authenticationFacade;
    private final FightMapper fightMapper;
    private final FightEffectsSingleton fightEffectsSingleton;

    public FightService(CharacterRepository characterRepository, SkillRepository skillRepository, IAuthenticationFacade authenticationFacade, FightMapper fightMapper) {
        this.characterRepository = characterRepository;
        this.skillRepository = skillRepository;
        this.authenticationFacade = authenticationFacade;
        this.fightMapper = fightMapper;
        this.fightEffectsSingleton = FightEffectsSingleton.getInstance();
    }

    @Transactional
    public FightDetails getFight(long characterId) {
        var character = characterRepository.getWithEntityGraph(characterId, Character.CHARACTER_FIGHT);
        authenticationFacade.checkIfCharacterBelongsToUser(character);
        return fightMapper.toDetails(character.getOccupation().getFight());
    }

    @Transactional
    public FightActionResponse performActionInFight(@NotNull FightActionRequest fightActionRequest) {
        fightEffectsSingleton.getPlayerStunned().set(false);
        fightEffectsSingleton.getEnemyStunned().set(false);
        fightEffectsSingleton.getPlayerDamageMultiplier().set(1f);
        fightEffectsSingleton.getEnemyDamageMultiplier().set(1f);
        fightEffectsSingleton.getPlayerDefenceMultiplier().set(1f);
        fightEffectsSingleton.getEnemyDefenceMultiplier().set(1f);
        var request = fightMapper.toDto(fightActionRequest);
        var character = characterRepository.getWithEntityGraph(request.getCharacterId(), Character.CHARACTER_FIGHT_ACTION);
        authenticationFacade.checkIfCharacterBelongsToUser(character);
        var fight = character.getOccupation().getFight();
        checkIfFightIsActive(fight);
        var response = new FightActionResponse();

        applyFightEffects(fight, character);

        if (!fightEffectsSingleton.getPlayerStunned().get()) {
            FightAction action = null;
            switch (request.getAction()) {
                case NORMAL_ATTACK -> action = new NormalAttack();
                case USE_POTION -> action = new UsePotion();
                case SPECIAL_ATTACK -> action = new SpecialAttack(skillRepository);
            }
            if (action != null) {
                action.performAction(character, fight, request, response);
            }
        }
        if (!fightEffectsSingleton.getEnemyStunned().get()) {
            enemyTurn(response, fight, character);
        }

        if (fight.getEnemyCurrentHp() <= 0) {
            winFight(fight, character, response);
        } else if (character.getStatistics().getCurrentHp() <= 0) {
            loseFight(fight, character, response);
        }

        if (response.getPlayerWon() == null) {
            character.getStatistics().regenerateManaPerTurn();
            character.getOccupation().getFight().regenerateEnemyManaPerTurn();
        }

        character = characterRepository.save(character);
        response.setPlayerCurrentMana(character.getStatistics().getCurrentMana());
        response.setPlayerCurrentHp(character.getStatistics().getCurrentHp());
        response.setFight(fightMapper.toDetails(character.getOccupation().getFight()));
        response.setPlayerPotions(character.getEquipment().getHealthPotions());
        return response;
    }

    private void applyFightEffects(@NotNull Fight fight, @NotNull Character character) {
        if (fight.getFightEffects() != null) {
            fight.getFightEffects().stream().filter(effect -> effect.getDuration() > 0).forEach(effect -> {
                effect.passTurn();
                switch (effect.getSkillEffect()) {
                    case BLEEDING, BURNING -> {
                        if (effect.isPlayerEffect()) {
                            character.getStatistics().takeDamage(
                                    Math.round(character.getStatistics().getMaxHp() * effect.getEffectMultiplier()));
                        } else {
                            var enemyMaxHp = Optional.ofNullable(fight.getEnemy()).map(Enemy::getHp).orElseThrow();
                            fight.dealDamageToEnemy(Math.round(enemyMaxHp * effect.getEffectMultiplier()));
                        }
                    }
                    case STUNNED -> {
                        if (effect.isPlayerEffect()) {
                            fightEffectsSingleton.getPlayerStunned().set(true);
                        } else {
                            fightEffectsSingleton.getEnemyStunned().set(true);
                        }
                    }
                    case WEAKNESS -> {
                        if (effect.isPlayerEffect()) {
                            fightEffectsSingleton.getPlayerDamageMultiplier().set(Math.max(0.1f, fightEffectsSingleton.getPlayerDamageMultiplier().get() - effect.getEffectMultiplier()));
                        } else {
                            fightEffectsSingleton.getEnemyDamageMultiplier().set(Math.max(0.1f, fightEffectsSingleton.getEnemyDamageMultiplier().get() - effect.getEffectMultiplier()));
                        }
                    }
                    case DAMAGE_BOOST -> {
                        if (effect.isPlayerEffect()) {
                            fightEffectsSingleton.getPlayerDamageMultiplier().set(Math.max(0.1f, fightEffectsSingleton.getPlayerDamageMultiplier().get() + effect.getEffectMultiplier()));
                        } else {
                            fightEffectsSingleton.getEnemyDamageMultiplier().set(Math.max(0.1f, fightEffectsSingleton.getEnemyDamageMultiplier().get() + effect.getEffectMultiplier()));
                        }
                    }
                    case DEFENCE_BOOST -> {
                        if (effect.isPlayerEffect()) {
                            fightEffectsSingleton.getPlayerDefenceMultiplier().set(Math.max(0.1f, fightEffectsSingleton.getPlayerDefenceMultiplier().get() + effect.getEffectMultiplier()));
                        } else {
                            fightEffectsSingleton.getEnemyDefenceMultiplier().set(Math.max(0.1f, fightEffectsSingleton.getEnemyDefenceMultiplier().get() + effect.getEffectMultiplier()));
                        }
                    }
                    case DAMAGE_DEFENCE_BOOST -> {
                        if (effect.isPlayerEffect()) {
                            fightEffectsSingleton.getPlayerDamageMultiplier().set(Math.max(0.1f, fightEffectsSingleton.getPlayerDamageMultiplier().get() + effect.getEffectMultiplier()));
                            fightEffectsSingleton.getPlayerDefenceMultiplier().set(Math.max(0.1f, fightEffectsSingleton.getPlayerDefenceMultiplier().get() + effect.getEffectMultiplier()));
                        } else {
                            fightEffectsSingleton.getEnemyDamageMultiplier().set(Math.max(0.1f, fightEffectsSingleton.getEnemyDamageMultiplier().get() + effect.getEffectMultiplier()));
                            fightEffectsSingleton.getEnemyDefenceMultiplier().set(Math.max(0.1f, fightEffectsSingleton.getEnemyDefenceMultiplier().get() + effect.getEffectMultiplier()));
                        }
                    }
                }
            });
        }
    }

    private void enemyTurn(@NotNull FightActionResponse response,
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

    private void winFight(@NotNull Fight fight, @NotNull Character character, @NotNull FightActionResponse response) {
        fight.setActive(false);
        fight.setEnemy(null);
        var adventure = character.getOccupation().getAdventure();
        if (adventure == null) {
            throw new AdventureNotFoundException();
        }
        character.getEquipment().earnGold(adventure.getGoldForAdventure());
        character.getStatistics().earnXp(adventure.getXpForAdventure());
        character.getOccupation().setAdventure(null);
        character.getOccupation().setFight(fight);
        if (fight.getFightEffects() != null) {
            // Tydzień 5, Iterator
            // Stworzona została nowa klasa, która w konstruktorze przyjmuje kolekcję fightEffects
            // Następnie możliwe jest iterowanie po tej kolekcji metodami hasNext i next
            var fightEffects = new CollectionOfFightEffects(fight.getFightEffects());
            for (Iterator iter = fightEffects.getIterator(); iter.hasNext(); ) {
                var effect = (FightEffect) iter.next();
                effect.setDuration(0);
            }
            // Koniec Tydzień 5, Iterator
            //fight.getFightEffects().forEach(fightEffect -> fightEffect.setDuration(0));
        }
        response.setPlayerWon(true);
    }

    private void loseFight(@NotNull Fight fight, @NotNull Character character, @NotNull FightActionResponse response) {
        fight.setActive(false);
        fight.setEnemy(null);
        character.getOccupation().setAdventure(null);
        character.getOccupation().setFight(fight);
        if (fight.getFightEffects() != null) {
            fight.getFightEffects().forEach(fightEffect -> fightEffect.setDuration(0));
        }
        response.setPlayerWon(false);
    }

    private void checkIfFightIsActive(@NotNull Fight fight) {
        if (!fight.isActive()) {
            throw new FightIsNotActiveException();
        }
    }
}

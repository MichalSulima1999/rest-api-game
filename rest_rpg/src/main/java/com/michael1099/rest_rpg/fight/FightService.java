package com.michael1099.rest_rpg.fight;

import com.michael1099.rest_rpg.auth.auth.IAuthenticationFacade;
import com.michael1099.rest_rpg.character.CharacterRepository;
import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.enemy.model.Enemy;
import com.michael1099.rest_rpg.enemy.model.StrategyElement;
import com.michael1099.rest_rpg.exceptions.AdventureNotFoundException;
import com.michael1099.rest_rpg.exceptions.FightIsNotActiveException;
import com.michael1099.rest_rpg.exceptions.NoPotionsLeftException;
import com.michael1099.rest_rpg.exceptions.NotEnoughManaException;
import com.michael1099.rest_rpg.exceptions.SkillNotFoundException;
import com.michael1099.rest_rpg.fight.model.Fight;
import com.michael1099.rest_rpg.fight_effect.FightEffect;
import com.michael1099.rest_rpg.skill.SkillRepository;
import com.michael1099.rest_rpg.statistics.Statistics;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.ElementEvent;
import org.openapitools.model.FightActionRequest;
import org.openapitools.model.FightActionResponse;
import org.openapitools.model.FightDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
public class FightService {

    private final CharacterRepository characterRepository;
    private final SkillRepository skillRepository;
    private final IAuthenticationFacade authenticationFacade;
    private final FightMapper fightMapper;

    @Transactional
    public FightDetails getFight(long characterId) {
        var character = characterRepository.getWithEntityGraph(characterId, Character.CHARACTER_FIGHT);
        authenticationFacade.checkIfCharacterBelongsToUser(character);
        return fightMapper.toDetails(character.getOccupation().getFight());
    }

    @Transactional
    public FightActionResponse performActionInFight(@NotNull FightActionRequest fightActionRequest) {
        var request = fightMapper.toDto(fightActionRequest);
        var character = characterRepository.getWithEntityGraph(request.getCharacterId(), Character.CHARACTER_FIGHT_ACTION);
        authenticationFacade.checkIfCharacterBelongsToUser(character);
        var fight = character.getOccupation().getFight();
        checkIfFightIsActive(fight);
        var response = new FightActionResponse();
        switch (request.getAction()) {
            case NORMAL_ATTACK -> attack(character, response, fight);
            case USE_POTION -> usePotion(character);
            case SPECIAL_ATTACK -> specialAttack(character, fight, response, fightActionRequest.getSkillId());
        }
        // TODO: Efekty w walce
        if (fight.getEnemyCurrentHp() <= 0) {
            winFight(fight, character, response);
        } else if (character.getStatistics().getCurrentHp() <= 0) {
            loseFight(fight, character, response);
        }
        character = characterRepository.save(character);
        response.setPlayerCurrentMana(character.getStatistics().getCurrentMana());
        response.setPlayerCurrentHp(character.getStatistics().getCurrentHp());
        response.setFight(fightMapper.toDetails(character.getOccupation().getFight()));
        response.setPlayerPotions(character.getEquipment().getHealthPotions());
        return response;
    }

    private void attack(@NotNull Character character, @NotNull FightActionResponse response, @NotNull Fight fight) {
        var playerStatistics = character.getStatistics();
        var enemy = Optional.ofNullable(fight).map(Fight::getEnemy).orElseThrow();

        var playerDamage = character.getStatistics().getDamage();
        if (new Random().nextFloat(0, 100) < playerStatistics.getCriticalChance()) {
            playerDamage *= 2;
            response.setPlayerCriticalStrike(true);
        }
        fight.dealDamageToEnemy(playerDamage);
        response.setPlayerDamage(playerDamage);
        enemyTurn(response, playerStatistics, enemy, fight, character);
        character.setStatistics(playerStatistics);
        character.getOccupation().setFight(fight);
    }

    private void usePotion(@NotNull Character character) {
        if (character.getEquipment().getHealthPotions() <= 0) {
            throw new NoPotionsLeftException();
        }
        character.usePotion();
    }

    private void specialAttack(@NotNull Character character, @NotNull Fight fight, @NotNull FightActionResponse response, long skillId) {
        var skill = skillRepository.get(skillId);
        var statistics = character.getStatistics();
        var skillLevel = character.getSkills().stream().filter(s -> s.getSkill().getId().equals(skill.getId())).findFirst().orElseThrow(SkillNotFoundException::new).getLevel();
        if (statistics.getCurrentMana() < skill.getManaCost()) {
            throw new NotEnoughManaException();
        }
        if (skill.getEffect() != null) {
            var effects = Optional.ofNullable(fight.getFightEffects()).orElse(new HashSet<>()).stream().
                    filter(fightEffect -> fightEffect.getDuration() <= 0).collect(Collectors.toSet());
            FightEffect fightEffect = new FightEffect();
            if (!effects.isEmpty()) {
                fightEffect = effects.stream().findFirst().get();
            }
            fightEffect.setFight(fight);
            int effectDuration = skill.getFinalEffectDuration(skillLevel);
            fightEffect.setDuration(effectDuration);
            fightEffect.setPlayerEffect(false);
            fightEffect.setSkillEffect(skill.getEffect());
        }
        var baseDamage = skill.isMagicDamage() ? statistics.getMagicDamage() : statistics.getDamage();
        var playerDamage = Math.round((skill.getDamageMultiplier(skillLevel) * baseDamage));
        statistics.useMana(skill.getManaCost());
        fight.dealDamageToEnemy(playerDamage);
        response.setPlayerDamage(playerDamage);
        response.setPlayerCurrentMana(statistics.getCurrentMana());
        character.setStatistics(statistics);
        enemyTurn(response, statistics, fight.getEnemy(), fight, character);
    }

    private void enemyTurn(@NotNull FightActionResponse response, @NotNull Statistics playerStatistics, @NotNull Enemy enemy, @NotNull Fight fight, @NotNull Character character) {
        if (fight.getEnemyCurrentHp() > 0) {
            var enemyAction = decideEnemyAction(fight, character);
            response.setEnemyAction(enemyAction.getElementAction());
            switch (enemyAction.getElementAction()) {
                case NORMAL_ATTACK -> enemyNormalAttack(response, playerStatistics, enemy);
                case SPECIAL_ATTACK -> enemySpecialAttack(response, playerStatistics, enemy, fight);
                case USE_POTION -> enemyUsePotion(response, playerStatistics, enemy, fight);
            }
        }
    }

    private void enemyNormalAttack(@NotNull FightActionResponse response, @NotNull Statistics playerStatistics, @NotNull Enemy enemy) {
        var successfulHit = new Random().nextFloat(0, 100) > playerStatistics.getDodgeChance();
        response.setEnemyHit(false);
        response.setEnemyDamage(0);
        if (successfulHit) {
            var enemyDamage = Math.max(1, enemy.getDamage() - playerStatistics.getArmor());
            playerStatistics.takeDamage(enemyDamage);
            response.setEnemyHit(true);
            response.setEnemyDamage(enemyDamage);
            response.setPlayerCurrentHp(playerStatistics.getCurrentHp());
        }
    }

    private void enemySpecialAttack(@NotNull FightActionResponse response, @NotNull Statistics playerStatistics, @NotNull Enemy enemy, @NotNull Fight fight) {
        var skill = enemy.getSkill();
        if (fight.getEnemyCurrentMana() < skill.getManaCost()) {
            enemyNormalAttack(response, playerStatistics, enemy);
        } else {
            if (skill.getEffect() != null) {
                var effects = Optional.ofNullable(fight.getFightEffects()).orElse(new HashSet<>()).stream().filter(fightEffect -> fightEffect.getDuration() <= 0).collect(Collectors.toSet());
                FightEffect fightEffect = new FightEffect();
                if (!effects.isEmpty()) {
                    fightEffect = effects.stream().findFirst().get();
                }
                fightEffect.setFight(fight);
                int effectDuration = Optional.ofNullable(skill.getEffectDuration()).orElseThrow();
                fightEffect.setDuration(effectDuration);
                fightEffect.setPlayerEffect(true);
                response.setEnemyHit(false);
                fightEffect.setSkillEffect(skill.getEffect());
                var successfulHit = new Random().nextFloat(0, 100) > playerStatistics.getDodgeChance();
                if (successfulHit) {
                    var enemyDamage = Math.max(1,
                            Math.round((enemy.getSkill().getMultiplier() + effectDuration * enemy.getSkillLevel()) *
                                    enemy.getDamage() + enemy.getDamage() - playerStatistics.getArmor()));
                    playerStatistics.takeDamage(enemyDamage);
                    fight.enemyUseMana();
                    response.setEnemyDamage(enemyDamage);
                    response.setEnemyHit(true);
                    response.setPlayerCurrentHp(playerStatistics.getCurrentHp());
                }
            }
        }
    }

    private void enemyUsePotion(@NotNull FightActionResponse response, @NotNull Statistics playerStatistics, @NotNull Enemy enemy, @NotNull Fight fight) {
        if (enemy.getNumberOfPotions() > 0) {
            fight.healEnemy();
            enemy.usePotion();
        } else {
            enemyNormalAttack(response, playerStatistics, enemy);
        }
    }

    private StrategyElement decideEnemyAction(@NotNull Fight fight, @NotNull Character character) {
        var enemy = Optional.ofNullable(fight.getEnemy()).orElseThrow();
        StrategyElement enemyAction;
        var strategy = enemy.getStrategyElements();
        var enemyHpPercent = (float) fight.getEnemyCurrentHp() / (float) enemy.getHp();
        var playerHpPercent = (float) character.getStatistics().getCurrentHp() / (float) character.getStatistics().getMaxHp();
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
        response.setPlayerWon(true);
    }

    private void loseFight(@NotNull Fight fight, @NotNull Character character, @NotNull FightActionResponse response) {
        fight.setActive(false);
        fight.setEnemy(null);
        character.getOccupation().setAdventure(null);
        character.getOccupation().setFight(fight);
        response.setPlayerWon(false);
    }

    private void checkIfFightIsActive(@NotNull Fight fight) {
        if (!fight.isActive()) {
            throw new FightIsNotActiveException();
        }
    }
}
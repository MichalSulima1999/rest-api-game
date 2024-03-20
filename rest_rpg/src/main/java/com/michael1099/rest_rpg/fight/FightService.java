package com.michael1099.rest_rpg.fight;

import com.michael1099.rest_rpg.auth.auth.IAuthenticationFacade;
import com.michael1099.rest_rpg.character.CharacterRepository;
import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.enemy.model.StrategyElement;
import com.michael1099.rest_rpg.exceptions.FightIsNotActiveException;
import com.michael1099.rest_rpg.fight.command.EndFight;
import com.michael1099.rest_rpg.fight.command.EndFightExecutor;
import com.michael1099.rest_rpg.fight.flyweight.Terrain;
import com.michael1099.rest_rpg.fight.helpers.FightAction;
import com.michael1099.rest_rpg.fight.helpers.FightEffectsSingleton;
import com.michael1099.rest_rpg.fight.helpers.NormalAttack;
import com.michael1099.rest_rpg.fight.helpers.SpecialAttack;
import com.michael1099.rest_rpg.fight.helpers.UsePotion;
import com.michael1099.rest_rpg.fight.model.Fight;
import com.michael1099.rest_rpg.fight_effect.strategy.FightEffectStrategy;
import com.michael1099.rest_rpg.fight_effect.strategy.FightEffectStrategyFactory;
import com.michael1099.rest_rpg.skill.SkillRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.openapitools.model.ElementEvent;
import org.openapitools.model.FightActionRequest;
import org.openapitools.model.FightActionResponse;
import org.openapitools.model.FightDetails;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.Random;

@Service
@Validated
public class FightService {

    public static int MANA_REGENERATION_PERCENT_PER_TURN = 10;
    public static int MAP_SIZE = 500;
    public static int ELEMENTS_TO_DRAW = 1000;
    static int OBJECT_TYPES = 3;

    private final CharacterRepository characterRepository;
    private final SkillRepository skillRepository;
    private final IAuthenticationFacade authenticationFacade;
    private final FightMapper fightMapper;
    private final FightEffectsSingleton fightEffectsSingleton;
    private final EndFightExecutor endFightExecutor;
    private final ApplicationEventPublisher eventPublisher;

    public FightService(CharacterRepository characterRepository, SkillRepository skillRepository, IAuthenticationFacade authenticationFacade, FightMapper fightMapper, ApplicationEventPublisher eventPublisher) {
        this.characterRepository = characterRepository;
        this.skillRepository = skillRepository;
        this.authenticationFacade = authenticationFacade;
        this.fightMapper = fightMapper;
        this.eventPublisher = eventPublisher;
        this.fightEffectsSingleton = FightEffectsSingleton.getInstance();
        this.endFightExecutor = new EndFightExecutor();
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

        Terrain terrain = new Terrain();
        Random random = new Random();
        for (int i = 0; i < (ELEMENTS_TO_DRAW / OBJECT_TYPES); i++) {
            terrain.placeObject(random.nextInt(0, MAP_SIZE),
                    random.nextInt(0, MAP_SIZE), "Oak", "Oak texture", true);
            terrain.placeObject(random.nextInt(0, MAP_SIZE),
                    random.nextInt(0, MAP_SIZE), "Stone", "Stone texture", true);
            terrain.placeObject(random.nextInt(0, MAP_SIZE),
                    random.nextInt(0, MAP_SIZE), "Grass", "Grass texture", false);
        }
        terrain.createTerrain();

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
            var endFight = new EndFight(fight, character, response, eventPublisher);
            endFightExecutor.executeCommand(endFight::winFight);
        } else if (character.getStatistics().getCurrentHp() <= 0) {
            var endFight = new EndFight(fight, character, response, eventPublisher);
            endFightExecutor.executeCommand(endFight::loseFight);
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
                FightEffectStrategy strategy = FightEffectStrategyFactory.getStrategy(effect.getSkillEffect());
                if (strategy != null) {
                    strategy.applyEffect(effect, fight, character);
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

    private void checkIfFightIsActive(@NotNull Fight fight) {
        if (!fight.isActive()) {
            throw new FightIsNotActiveException();
        }
    }
}

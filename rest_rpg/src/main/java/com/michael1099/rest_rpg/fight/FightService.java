package com.michael1099.rest_rpg.fight;

import com.michael1099.rest_rpg.auth.auth.IAuthenticationFacade;
import com.michael1099.rest_rpg.character.CharacterRepository;
import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.exceptions.FightIsNotActiveException;
import com.michael1099.rest_rpg.fight.command.EndFight;
import com.michael1099.rest_rpg.fight.command.EndFightExecutor;
import com.michael1099.rest_rpg.fight.model.Fight;
import com.michael1099.rest_rpg.fight_effect.strategy.FightEffectStrategy;
import com.michael1099.rest_rpg.fight_effect.strategy.FightEffectStrategyFactory;
import com.michael1099.rest_rpg.skill.SkillRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.openapitools.model.FightActionRequest;
import org.openapitools.model.FightActionResponse;
import org.openapitools.model.FightDetails;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class FightService {

    public static int MANA_REGENERATION_PERCENT_PER_TURN = 10;

    private final CharacterRepository characterRepository;
    private final IAuthenticationFacade authenticationFacade;
    private final FightMapper fightMapper;
    private final EndFightExecutor endFightExecutor;
    private final ApplicationEventPublisher eventPublisher;
    // Tydzień 7 - Zastosuj zasadę Pojedynczej odpowiedzialności
    // Stworzone zostały 3 klasy: terrainManager do zarządzania generowanym terenem, fightEffectsManager do zarządzania efektami,
    // enemyFight do zarządzania akcjami przeciwnika
    private final TerrainManager terrainManager;
    private final FightEffectsManager fightEffectsManager;
    private final EnemyFight enemyFight;
    // Koniec Tydzień 7 - Zastosuj zasadę Pojedynczej odpowiedzialności

    public FightService(CharacterRepository characterRepository, SkillRepository skillRepository, IAuthenticationFacade authenticationFacade, FightMapper fightMapper, ApplicationEventPublisher eventPublisher) {
        this.characterRepository = characterRepository;
        this.authenticationFacade = authenticationFacade;
        this.fightMapper = fightMapper;
        this.eventPublisher = eventPublisher;
        this.endFightExecutor = new EndFightExecutor();
        this.terrainManager = new TerrainManagerImplementation();
        this.enemyFight = new EnemyFightImplementation();
        this.fightEffectsManager = new FightEffectsManagerImplementation(skillRepository, this.enemyFight);
    }

    @Transactional
    public FightDetails getFight(long characterId) {
        var character = characterRepository.getWithEntityGraph(characterId, Character.CHARACTER_FIGHT);
        authenticationFacade.checkIfCharacterBelongsToUser(character);
        return fightMapper.toDetails(character.getOccupation().getFight());
    }

    @Transactional
    public FightActionResponse performActionInFight(@NotNull FightActionRequest fightActionRequest) {
        fightEffectsManager.resetEffects();
        var request = fightMapper.toDto(fightActionRequest);
        var character = characterRepository.getWithEntityGraph(request.getCharacterId(), Character.CHARACTER_FIGHT_ACTION);
        authenticationFacade.checkIfCharacterBelongsToUser(character);
        var fight = character.getOccupation().getFight();
        checkIfFightIsActive(fight);
        var response = new FightActionResponse();
        terrainManager.generateTerrain();
        applyFightEffects(fight, character);

        fightEffectsManager.checkForStunned(request, character, response);
        checkIfFightEnded(fight, character, response);

        if (response.getPlayerWon() == null) {
            character.getStatistics().regenerateManaPerTurn();
            character.getOccupation().getFight().regenerateEnemyManaPerTurn();
        }

        character = characterRepository.save(character);
        return prepareResponse(character, response);
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

    private void checkIfFightIsActive(@NotNull Fight fight) {
        if (!fight.isActive()) {
            throw new FightIsNotActiveException();
        }
    }

    private void checkIfFightEnded(Fight fight, Character character, FightActionResponse response) {
        if (fight.getEnemyCurrentHp() <= 0) {
            var endFight = new EndFight(fight, character, response, eventPublisher);
            endFightExecutor.executeCommand(endFight::winFight);
        } else if (character.getStatistics().getCurrentHp() <= 0) {
            var endFight = new EndFight(fight, character, response, eventPublisher);
            endFightExecutor.executeCommand(endFight::loseFight);
        }
    }

    private FightActionResponse prepareResponse(Character character, FightActionResponse response) {
        response.setPlayerCurrentMana(character.getStatistics().getCurrentMana());
        response.setPlayerCurrentHp(character.getStatistics().getCurrentHp());
        response.setFight(fightMapper.toDetails(character.getOccupation().getFight()));
        response.setPlayerPotions(character.getEquipment().getHealthPotions());
        return response;
    }
}

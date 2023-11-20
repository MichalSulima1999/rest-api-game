package com.michael1099.rest_rpg.adventure;

import com.michael1099.rest_rpg.adventure.model.Adventure;
import com.michael1099.rest_rpg.auth.auth.IAuthenticationFacade;
import com.michael1099.rest_rpg.character.CharacterRepository;
import com.michael1099.rest_rpg.enemy.EnemyRepository;
import com.michael1099.rest_rpg.exceptions.AdventureNameExistsException;
import com.michael1099.rest_rpg.exceptions.AdventureNotFoundException;
import com.michael1099.rest_rpg.exceptions.CharacterIsNotOnAdventureException;
import com.michael1099.rest_rpg.exceptions.CharacterStillOnAdventureException;
import com.michael1099.rest_rpg.exceptions.FightIsOngoingException;
import com.michael1099.rest_rpg.fight.model.Fight;
import com.michael1099.rest_rpg.helpers.SearchHelper;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.AdventureBasicPage;
import org.openapitools.model.AdventureCreateRequest;
import org.openapitools.model.AdventureDetails;
import org.openapitools.model.AdventureLite;
import org.openapitools.model.AdventureSearchRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class AdventureService {

    private final AdventureRepository adventureRepository;
    private final EnemyRepository enemyRepository;
    private final CharacterRepository characterRepository;
    private final IAuthenticationFacade authenticationFacade;
    private final AdventureMapper mapper;

    @Transactional
    public AdventureLite createAdventure(@NotNull AdventureCreateRequest request) {
        var dto = mapper.toDto(request);
        checkIfAdventureExists(dto.getName());
        var enemy = enemyRepository.getById(dto.getEnemy());
        var adventure = Adventure.of(dto, enemy);
        return mapper.toLite(adventureRepository.save(adventure));
    }

    @Transactional
    public AdventureLite editAdventure(long adventureId, @NotNull AdventureCreateRequest request) {
        var dto = mapper.toDto(request);
        checkIfEditedAdventureExists(dto.getName(), adventureId);
        var enemy = enemyRepository.getById(dto.getEnemy());
        var adventure = adventureRepository.getAdventureById(adventureId);
        adventure.modify(dto, enemy);
        return mapper.toLite(adventureRepository.save(adventure));
    }

    @Transactional
    public AdventureLite deleteAdventure(long adventureId) {
        var adventure = adventureRepository.getAdventureById(adventureId);
        adventure.setDeleted(true);
        return mapper.toLite(adventureRepository.save(adventure));
    }

    @Transactional
    public AdventureBasicPage findAdventures(@NotNull AdventureSearchRequest request) {
        var pageable = SearchHelper.getPageable(request.getPagination());
        return mapper.toPage(adventureRepository.findAdventures(request, pageable));
    }

    @Transactional
    public AdventureDetails getAdventure(long adventureId) {
        return mapper.toDetails(adventureRepository.getAdventureById(adventureId));
    }

    @Transactional
    public AdventureLite startAdventure(long adventureId, long characterId) {
        var character = characterRepository.getCharacterById(characterId);
        authenticationFacade.checkIfCharacterBelongsToUser(character);
        character.getOccupation().throwIfCharacterIsOccupied();

        var adventure = adventureRepository.getAdventureById(adventureId);
        character.getOccupation().startAdventure(adventure);
        characterRepository.save(character);

        return mapper.toLite(adventure);
    }

    @Transactional
    public AdventureLite endAdventure(long characterId) {
        var character = characterRepository.getCharacterById(characterId);
        authenticationFacade.checkIfCharacterBelongsToUser(character);
        if (Optional.ofNullable(character.getOccupation().getFinishTime()).orElseThrow(AdventureNotFoundException::new).isAfter(LocalDateTime.now())) {
            throw new CharacterStillOnAdventureException();
        }
        checkIfFightIsOngoing(character.getOccupation().getFight());
        var adventure = Optional.ofNullable(character.getOccupation().getAdventure())
                .orElseThrow(CharacterIsNotOnAdventureException::new);

        character.getOccupation().endAdventure(adventure);
        characterRepository.save(character);

        return mapper.toLite(adventure);
    }

    private void checkIfAdventureExists(@NotBlank String adventureName) {
        if (adventureRepository.existsByNameIgnoreCaseAndDeletedFalse(adventureName)) {
            throw new AdventureNameExistsException();
        }
    }

    private void checkIfEditedAdventureExists(@NotBlank String adventureName, long adventureId) {
        var optionalAdventure = adventureRepository.findByNameIgnoreCaseAndDeletedFalse(adventureName);
        if (optionalAdventure.isPresent() && !optionalAdventure.get().getId().equals(adventureId)) {
            throw new AdventureNameExistsException();
        }
    }

    private void checkIfFightIsOngoing(@NotNull Fight fight) {
        if (fight.isActive()) {
            throw new FightIsOngoingException();
        }
    }
}

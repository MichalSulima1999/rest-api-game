package com.michael1099.rest_rpg.adventure;

import com.michael1099.rest_rpg.adventure.model.Adventure;
import com.michael1099.rest_rpg.enemy.EnemyRepository;
import com.michael1099.rest_rpg.exceptions.AdventureNameExistsException;
import com.michael1099.rest_rpg.helpers.SearchHelper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.AdventureBasicPage;
import org.openapitools.model.AdventureCreateRequest;
import org.openapitools.model.AdventureLite;
import org.openapitools.model.AdventureSearchRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class AdventureService {

    private final AdventureRepository adventureRepository;
    private final EnemyRepository enemyRepository;
    private final AdventureMapper mapper;

    public AdventureLite createAdventure(@NotNull AdventureCreateRequest request) {
        var dto = mapper.toDto(request);
        checkIfAdventureExists(dto.getName());
        var enemy = enemyRepository.getById(dto.getEnemy());
        var adventure = Adventure.of(dto, enemy);
        return mapper.toLite(adventureRepository.save(adventure));
    }

    public AdventureBasicPage findAdventures(@NotNull AdventureSearchRequest request) {
        var pageable = SearchHelper.getPageable(request.getPagination());
        return mapper.toPage(adventureRepository.findAdventures(request, pageable));
    }

    private void checkIfAdventureExists(@NotBlank String adventureName) {
        if (adventureRepository.existsByNameIgnoreCase(adventureName)) {
            throw new AdventureNameExistsException();
        }
    }
}

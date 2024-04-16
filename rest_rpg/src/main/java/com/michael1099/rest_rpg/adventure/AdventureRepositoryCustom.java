package com.michael1099.rest_rpg.adventure;

import com.michael1099.rest_rpg.adventure.model.Adventure;
import jakarta.validation.constraints.NotNull;
import org.openapitools.model.AdventureSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface AdventureRepositoryCustom {

    Page<Adventure> findAdventures(@NotNull AdventureSearchRequest request, @NotNull Pageable pageable);

    long deleteAdventure(@NotNull long adventureId);
}

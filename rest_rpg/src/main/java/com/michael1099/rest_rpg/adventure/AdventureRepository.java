package com.michael1099.rest_rpg.adventure;

import com.michael1099.rest_rpg.adventure.model.Adventure;
import com.michael1099.rest_rpg.exceptions.AdventureNotFoundException;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdventureRepository extends JpaRepository<Adventure, Long>, AdventureRepositoryCustom {

    boolean existsByNameIgnoreCase(@NotBlank String name);

    @EntityGraph(value = Adventure.ADVENTURE_DETAILS)
    default Adventure getAdventureById(long adventureId) {
        return findById(adventureId).orElseThrow(AdventureNotFoundException::new);
    }
}

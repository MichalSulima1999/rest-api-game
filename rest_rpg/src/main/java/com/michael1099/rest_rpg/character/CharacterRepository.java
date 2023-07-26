package com.michael1099.rest_rpg.character;

import com.michael1099.rest_rpg.character.model.Character;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterRepository extends JpaRepository<Character, Long> {

    boolean existsByNameIgnoreCase(@NotNull String name);
}

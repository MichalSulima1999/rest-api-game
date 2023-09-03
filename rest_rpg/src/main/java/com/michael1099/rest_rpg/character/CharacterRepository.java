package com.michael1099.rest_rpg.character;

import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.exceptions.CharacterNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CharacterRepository extends JpaRepository<Character, Long> {

    boolean existsByNameIgnoreCase(@NotNull String name);

    @EntityGraph(Character.CHARACTER_BASIC)
    List<Character> findByUser_Username(@NotNull String username);

    @Override
    @NotNull
    @EntityGraph(Character.CHARACTER_BASIC)
    Optional<Character> findById(@NotNull Long id);

    default Character getCharacterById(long id) {
        return findById(id).orElseThrow(CharacterNotFoundException::new);
    }
}

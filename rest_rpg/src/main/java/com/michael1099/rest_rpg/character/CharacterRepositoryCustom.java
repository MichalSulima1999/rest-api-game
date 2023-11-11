package com.michael1099.rest_rpg.character;

import com.michael1099.rest_rpg.character.model.Character;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRepositoryCustom {

    Character getWithEntityGraph(long id, @NotEmpty String graph);
}

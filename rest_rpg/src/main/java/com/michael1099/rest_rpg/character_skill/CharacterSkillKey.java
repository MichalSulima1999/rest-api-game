package com.michael1099.rest_rpg.character_skill;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class CharacterSkillKey implements Serializable {

    @Column(name = "character_id")
    private Long characterId;

    @Column(name = "skill_id")
    private Long skillId;
}

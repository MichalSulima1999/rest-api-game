package com.michael1099.rest_rpg.character_skill;

import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.skill.model.Skill;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CharacterSkill {

    @EmbeddedId
    private CharacterSkillKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("characterId")
    @JoinColumn(name = "character_id")
    private Character character;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("skillId")
    @JoinColumn(name = "skill_id")
    private Skill skill;

    private int level;

    private boolean deleted;
}

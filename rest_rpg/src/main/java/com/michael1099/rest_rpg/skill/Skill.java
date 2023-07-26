package com.michael1099.rest_rpg.skill;

import com.michael1099.rest_rpg.character.model.CharacterRace;
import com.michael1099.rest_rpg.character_skill.CharacterSkill;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SkillType type;

    private float multiplier;

    @Nullable
    @Enumerated(EnumType.STRING)
    private SkillEffect effect;

    @Nullable
    private Integer effectDuration;

    @Nullable
    private Float effectMultiplier;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CharacterRace race;

    @OneToMany(mappedBy = "skill", fetch = FetchType.LAZY)
    private Set<CharacterSkill> characters;

    private boolean deleted;
}

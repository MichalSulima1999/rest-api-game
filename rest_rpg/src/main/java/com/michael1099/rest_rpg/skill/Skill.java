package com.michael1099.rest_rpg.skill;

import com.michael1099.rest_rpg.character.model.CharacterClass;
import com.michael1099.rest_rpg.character_skill.CharacterSkill;
import com.michael1099.rest_rpg.enemy.model.Enemy;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    private CharacterClass characterClass;

    @OneToMany(mappedBy = "skill", fetch = FetchType.LAZY)
    private Set<Enemy> enemy = new HashSet<>();
    ;

    @OneToMany(mappedBy = "skill", fetch = FetchType.LAZY)
    private Set<CharacterSkill> characters = new HashSet<>();
    ;

    private boolean deleted;
}

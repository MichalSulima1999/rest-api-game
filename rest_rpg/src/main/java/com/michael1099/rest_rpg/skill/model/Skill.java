package com.michael1099.rest_rpg.skill.model;

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
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openapitools.model.CharacterClass;
import org.openapitools.model.SkillEffect;
import org.openapitools.model.SkillType;

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

    private int manaCost;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SkillType type;

    private float multiplier;

    @Nullable
    private Float multiplierPerLevel;

    @Nullable
    @Enumerated(EnumType.STRING)
    private SkillEffect effect;

    @Nullable
    private Integer effectDuration;

    @Nullable
    private Integer effectDurationPerLevel;

    @Nullable
    private Float effectMultiplier;

    @Nullable
    private Float effectMultiplierPerLevel;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CharacterClass characterClass;

    @OneToMany(mappedBy = "skill", fetch = FetchType.LAZY)
    private Set<Enemy> enemy = new HashSet<>();

    @OneToMany(mappedBy = "skill", fetch = FetchType.LAZY)
    private Set<CharacterSkill> characters = new HashSet<>();

    private boolean deleted;

    public static Skill of(@Valid SkillCreateRequestDto dto) {
        return builder()
                .name(dto.getName())
                .manaCost(dto.getManaCost())
                .type(dto.getType())
                .multiplier(dto.getMultiplier())
                .multiplierPerLevel(dto.getMultiplierPerLevel())
                .effect(dto.getEffect())
                .effectDuration(dto.getEffectDuration())
                .effectDurationPerLevel(dto.getEffectDurationPerLevel())
                .effectMultiplier(dto.getEffectMultiplier())
                .effectMultiplierPerLevel(dto.getEffectMultiplierPerLevel())
                .characterClass(dto.getCharacterClass())
                .build();
    }
}

package com.michael1099.rest_rpg.skill.model;

import com.michael1099.rest_rpg.character_skill.CharacterSkill;
import com.michael1099.rest_rpg.enemy.model.Enemy;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
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

    @OneToMany(mappedBy = "skill", fetch = FetchType.LAZY)
    private Set<CharacterSkill> characters = new HashSet<>();

    private boolean deleted;

    public static Skill of(@Valid SkillCreateRequestDto dto) {
        return builder()
                .name(dto.getName())
                .type(dto.getType())
                .multiplier(dto.getMultiplier())
                .effect(dto.getEffect())
                .effectDuration(dto.getEffectDuration())
                .effectMultiplier(dto.getEffectMultiplier())
                .characterClass(dto.getCharacterClass())
                .build();
    }
}

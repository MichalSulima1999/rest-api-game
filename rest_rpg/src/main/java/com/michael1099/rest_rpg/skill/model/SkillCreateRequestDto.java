package com.michael1099.rest_rpg.skill.model;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.openapitools.model.CharacterClass;
import org.openapitools.model.SkillEffect;
import org.openapitools.model.SkillType;

@Value
public class SkillCreateRequestDto {

    @NotBlank
    String name;

    int manaCost;

    @NotNull
    SkillType type;

    float multiplier;

    @Nullable
    Float multiplierPerLevel;

    @Nullable
    SkillEffect effect;

    @Nullable
    Integer effectDuration;

    @Nullable
    Integer effectDurationPerLevel;

    @Nullable
    Float effectMultiplier;

    @Nullable
    Float effectMultiplierPerLevel;

    @NotNull
    CharacterClass characterClass;
}
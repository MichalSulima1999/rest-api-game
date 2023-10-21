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

    @NotNull
    SkillType type;

    float multiplier;

    @Nullable
    SkillEffect effect;

    @Nullable
    Integer effectDuration;

    @Nullable
    Float effectMultiplier;

    @NotNull
    CharacterClass characterClass;
}

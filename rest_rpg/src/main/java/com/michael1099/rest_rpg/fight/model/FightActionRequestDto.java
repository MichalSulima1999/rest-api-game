package com.michael1099.rest_rpg.fight.model;

import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.openapitools.model.ElementAction;

@Value
public class FightActionRequestDto {

    long characterId;

    @NotNull
    ElementAction action;

    Long skillId;
}

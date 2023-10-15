package com.michael1099.rest_rpg.enemy.model;

import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.openapitools.model.ElementAction;
import org.openapitools.model.ElementEvent;

@Value
public class StrategyElementCreateRequestDto {

    @NotNull
    ElementEvent event;

    @NotNull
    ElementAction action;

    int priority;
}

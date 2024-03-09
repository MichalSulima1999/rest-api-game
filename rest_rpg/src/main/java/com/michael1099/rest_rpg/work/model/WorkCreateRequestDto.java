package com.michael1099.rest_rpg.work.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Value;
import org.openapitools.model.ResourceType;

@Value
public class WorkCreateRequestDto {

    @NotBlank
    String name;

    @NotNull
    ResourceType resourceType;

    @Positive
    int resourceAmount;

    @Positive
    int workMinutes;
}

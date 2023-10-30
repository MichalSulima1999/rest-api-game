package com.michael1099.rest_rpg.statistics.dto;

import jakarta.validation.constraints.Min;
import lombok.Value;

@Value
public class StatisticsUpdateRequestDto {

    @Min(0)
    int strength;

    @Min(0)
    int dexterity;

    @Min(0)
    int constitution;

    @Min(0)
    int intelligence;
}

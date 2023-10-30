package com.michael1099.rest_rpg.character.model.dto;

import com.michael1099.rest_rpg.character.model.CharacterArtwork;
import com.michael1099.rest_rpg.statistics.dto.StatisticsUpdateRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.openapitools.model.CharacterClass;
import org.openapitools.model.CharacterRace;
import org.openapitools.model.CharacterSex;

@Value
public class CharacterCreateRequestDto {

    @NotNull
    String name;

    @NotNull
    CharacterRace race;

    @NotNull
    CharacterSex sex;

    @NotNull
    CharacterClass characterClass;

    @NotNull
    CharacterArtwork artwork;

    @NotNull
    @Valid
    StatisticsUpdateRequestDto statistics;
}

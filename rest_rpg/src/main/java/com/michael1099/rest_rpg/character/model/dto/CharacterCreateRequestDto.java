package com.michael1099.rest_rpg.character.model.dto;

import com.michael1099.rest_rpg.character.model.CharacterArtwork;
import com.michael1099.rest_rpg.character.model.CharacterClass;
import com.michael1099.rest_rpg.character.model.CharacterRace;
import com.michael1099.rest_rpg.character.model.CharacterSex;
import com.michael1099.rest_rpg.statistics.dto.StatisticsUpdateRequestDto;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
    StatisticsUpdateRequestDto statistics;
}

package com.michael1099.rest_rpg.character;

import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.character.model.dto.CharacterCreateRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapitools.model.CharacterBasic;
import org.openapitools.model.CharacterBasics;
import org.openapitools.model.CharacterCreateRequest;
import org.openapitools.model.CharacterLite;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CharacterMapper {

    CharacterCreateRequestDto toDto(CharacterCreateRequest source);

    CharacterLite toLite(Character source);

    @Mapping(target = "content", source = "source")
    CharacterBasics toBasics(List<Character> source, Integer dummy);

    CharacterBasic toBasic(Character source);
}

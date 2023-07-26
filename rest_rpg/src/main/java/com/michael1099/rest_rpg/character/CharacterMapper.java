package com.michael1099.rest_rpg.character;

import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.character.model.dto.CharacterCreateRequestDto;
import org.mapstruct.Mapper;
import org.openapitools.model.CharacterCreateRequest;
import org.openapitools.model.CharacterLite;

@Mapper(componentModel = "spring")
public interface CharacterMapper {

    CharacterCreateRequestDto toDto(CharacterCreateRequest source);

    CharacterLite toLite(Character source);
}

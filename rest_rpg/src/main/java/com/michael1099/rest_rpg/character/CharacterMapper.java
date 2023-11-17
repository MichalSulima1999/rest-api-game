package com.michael1099.rest_rpg.character;

import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.character.model.dto.CharacterCreateRequestDto;
import com.michael1099.rest_rpg.character_skill.CharacterSkill;
import com.michael1099.rest_rpg.skill.SkillMapper;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.openapitools.model.CharacterBasic;
import org.openapitools.model.CharacterBasics;
import org.openapitools.model.CharacterCreateRequest;
import org.openapitools.model.CharacterDetails;
import org.openapitools.model.CharacterLite;
import org.openapitools.model.SkillDetails;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface CharacterMapper {

    CharacterCreateRequestDto toDto(CharacterCreateRequest source);

    CharacterLite toLite(Character source);

    @Mapping(target = "content", source = "source")
    CharacterBasics toBasics(List<Character> source, Integer dummy);

    CharacterBasic toBasic(Character source);

    @Mapping(target = "skills", expression = "java(toDetailsList(source.getSkills()))")
    CharacterDetails toDetails(@NotNull Character source);

    default List<SkillDetails> toDetailsList(@NotNull Set<CharacterSkill> source) {
        return source.stream().map(CharacterSkill::getSkill).map(s -> Mappers.getMapper(SkillMapper.class).toDetails(s)).toList();
    }
}

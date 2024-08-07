package com.michael1099.rest_rpg.skill;

import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.character_skill.CharacterSkill;
import com.michael1099.rest_rpg.skill.model.Skill;
import com.michael1099.rest_rpg.skill.model.SkillCreateRequestDto;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.Mapper;
import org.openapitools.model.CharacterSkillBasic;
import org.openapitools.model.CharacterSkillBasics;
import org.openapitools.model.SkillBasic;
import org.openapitools.model.SkillBasicPage;
import org.openapitools.model.SkillCreateRequest;
import org.openapitools.model.SkillDetails;
import org.openapitools.model.SkillLite;
import org.openapitools.model.SkillLites;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SkillMapper {

    SkillLite toLite(@NotNull Skill source);

    SkillBasic toBasic(@NotNull Skill source);

    SkillDetails toDetails(@NotNull Skill source);

    SkillCreateRequestDto toDto(@NotNull SkillCreateRequest source);

    SkillBasicPage toPage(@NotNull Page<Skill> source);

    CharacterSkillBasic toCharacterSkillBasic(@NotNull CharacterSkill source);

    default CharacterSkillBasics toCharacterSkillBasics(@NotNull Character source) {
        // Tydzień 10 - strumienie 1
        // Ten strumień pozwala na zmapowanie Set<CharacterSkill> na List<CharacterSkillBasic>
        return new CharacterSkillBasics().content(source.getSkills().stream().map(this::toCharacterSkillBasic).toList());
        // Koniec Tydzień 10 - strumienie
    }

    default SkillLites toLites(@NotNull List<Skill> source) {
        return new SkillLites().content(source.stream().map(this::toLite).toList());
    }
}

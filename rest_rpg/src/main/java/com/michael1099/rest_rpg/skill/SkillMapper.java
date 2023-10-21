package com.michael1099.rest_rpg.skill;

import com.michael1099.rest_rpg.skill.model.Skill;
import com.michael1099.rest_rpg.skill.model.SkillCreateRequestDto;
import org.mapstruct.Mapper;
import org.openapitools.model.SkillCreateRequest;
import org.openapitools.model.SkillLite;

@Mapper(componentModel = "spring")
public interface SkillMapper {

    SkillLite toLite(Skill source);

    SkillCreateRequestDto toDto(SkillCreateRequest source);
}

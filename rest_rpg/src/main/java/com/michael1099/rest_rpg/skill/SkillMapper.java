package com.michael1099.rest_rpg.skill;

import com.michael1099.rest_rpg.skill.model.Skill;
import com.michael1099.rest_rpg.skill.model.SkillCreateRequestDto;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.Mapper;
import org.openapitools.model.SkillBasic;
import org.openapitools.model.SkillCreateRequest;
import org.openapitools.model.SkillDetails;
import org.openapitools.model.SkillLite;

@Mapper(componentModel = "spring")
public interface SkillMapper {

    SkillLite toLite(@NotNull Skill source);

    SkillBasic toBasic(@NotNull Skill source);

    SkillDetails toDetails(@NotNull Skill source);

    SkillCreateRequestDto toDto(@NotNull SkillCreateRequest source);
}

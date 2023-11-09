package com.michael1099.rest_rpg.fight;

import com.michael1099.rest_rpg.fight.model.Fight;
import com.michael1099.rest_rpg.fight.model.FightActionRequestDto;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.Mapper;
import org.openapitools.model.FightActionRequest;
import org.openapitools.model.FightDetails;
import org.openapitools.model.FightLite;

@Mapper(componentModel = "spring")
public interface FightMapper {

    FightLite toLite(@NotNull Fight source);

    FightDetails toDetails(@NotNull Fight source);

    FightActionRequestDto toDto(@NotNull FightActionRequest source);
}

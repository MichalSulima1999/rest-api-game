package com.michael1099.rest_rpg.statistics;

import com.michael1099.rest_rpg.statistics.dto.StatisticsUpdateRequestDto;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.Mapper;
import org.openapitools.model.StatisticsDetails;
import org.openapitools.model.StatisticsLite;
import org.openapitools.model.StatisticsUpdateRequest;

@Mapper(componentModel = "spring")
public interface StatisticsMapper {

    StatisticsDetails toStatisticsDetails(@NotNull Statistics source);

    StatisticsLite toLite(@NotNull Statistics source);

    StatisticsUpdateRequestDto toStatisticsUpdateRequestDto(@NotNull StatisticsUpdateRequest source);
}

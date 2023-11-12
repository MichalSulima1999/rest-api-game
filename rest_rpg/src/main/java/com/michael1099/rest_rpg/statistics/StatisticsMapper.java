package com.michael1099.rest_rpg.statistics;

import com.michael1099.rest_rpg.statistics.dto.StatisticsUpdateRequestDto;
import org.mapstruct.Mapper;
import org.openapitools.model.StatisticsDetails;
import org.openapitools.model.StatisticsUpdateRequest;

@Mapper(componentModel = "spring")
public interface StatisticsMapper {

    StatisticsDetails toStatisticsDetails(Statistics source);

    StatisticsUpdateRequestDto toStatisticsUpdateRequestDto(StatisticsUpdateRequest source);
}

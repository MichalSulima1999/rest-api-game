package com.michael1099.rest_rpg.statistics;

import org.mapstruct.Mapper;
import org.openapitools.model.StatisticsDetails;

@Mapper(componentModel = "spring")
public interface StatisticsMapper {

    StatisticsDetails toStatisticsDetails(Statistics source);
}

package com.michael1099.rest_rpg.enemy;

import com.michael1099.rest_rpg.enemy.model.Enemy;
import com.michael1099.rest_rpg.enemy.model.EnemyCreateRequestDto;
import com.michael1099.rest_rpg.enemy.model.StrategyElementCreateRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.openapitools.model.EnemyCreateRequest;
import org.openapitools.model.EnemyLite;
import org.openapitools.model.StrategyElementCreateRequest;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface EnemyMapper {

    @Mapping(source = "enemyStrategy", target = "strategyElementCreateRequest", qualifiedByName = "strategyElementCreateRequestDto")
    EnemyCreateRequestDto toDto(EnemyCreateRequest source);

    EnemyLite toLite(Enemy source);

    StrategyElementCreateRequestDto toDto(StrategyElementCreateRequest source);

    @Named("strategyElementCreateRequestDto")
    default List<StrategyElementCreateRequestDto> strategyElementCreateRequestDto(List<StrategyElementCreateRequest> requests) {
        return requests.stream().map(this::toDto).collect(Collectors.toList());
    }
}
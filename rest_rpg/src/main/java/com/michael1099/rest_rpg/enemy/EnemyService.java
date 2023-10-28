package com.michael1099.rest_rpg.enemy;

import com.michael1099.rest_rpg.enemy.model.Enemy;
import com.michael1099.rest_rpg.enemy.model.StrategyElement;
import com.michael1099.rest_rpg.enemy.model.StrategyElementCreateRequestDto;
import com.michael1099.rest_rpg.exceptions.EnemyAlreadyExistsException;
import com.michael1099.rest_rpg.skill.SkillRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.EnemyCreateRequest;
import org.openapitools.model.EnemyLite;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
public class EnemyService {

    private final EnemyRepository enemyRepository;
    private final StrategyElementRepository strategyElementRepository;
    private final EnemyMapper enemyMapper;
    private final SkillRepository skillRepository;

    @Transactional
    public EnemyLite createEnemy(@NotNull EnemyCreateRequest enemyCreateRequest) {
        var dto = enemyMapper.toDto(enemyCreateRequest);
        checkIfEnemyExists(dto.getName());
        var skill = skillRepository.get(dto.getSkillId());
        var enemy = Enemy.of(dto, skill);
        enemy.setStrategyElements(addExistingStrategies(dto.getStrategyElementCreateRequest()));
        enemy = enemyRepository.save(enemy);
        return enemyMapper.toLite(enemy);
    }

    private void checkIfEnemyExists(@NotEmpty String enemyName) {
        if (enemyRepository.existsByName(enemyName)) {
            throw new EnemyAlreadyExistsException();
        }
    }

    private Set<StrategyElement> addExistingStrategies(@NotEmpty List<StrategyElementCreateRequestDto> strategyElementCreateRequest) {
        return strategyElementCreateRequest.stream().map(element ->
                strategyElementRepository.findByElementEventAndElementActionAndPriority(element.getEvent(), element.getAction(), element.getPriority())
                        .orElse(StrategyElement.builder()
                                .elementAction(element.getAction())
                                .elementEvent(element.getEvent())
                                .priority(element.getPriority())
                                .build())
        ).collect(Collectors.toSet());
    }
}

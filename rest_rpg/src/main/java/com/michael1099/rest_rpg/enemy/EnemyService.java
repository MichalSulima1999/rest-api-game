package com.michael1099.rest_rpg.enemy;

import com.michael1099.rest_rpg.enemy.model.Enemy;
import com.michael1099.rest_rpg.exceptions.EnemyAlreadyExistsException;
import com.michael1099.rest_rpg.skill.SkillRepository;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.ElementAction;
import org.openapitools.model.ElementEvent;
import org.openapitools.model.EnemyCreateRequest;
import org.openapitools.model.EnemyLite;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
public class EnemyService {

    private final EnemyRepository enemyRepository;
    private final EnemyMapper enemyMapper;
    private final SkillRepository skillRepository;

    public List<String> getStrategyActionsEnum() {
        return Arrays.stream(ElementAction.values()).map(Objects::toString).collect(Collectors.toList());
    }

    public List<String> getStrategyEventsEnum() {
        return Arrays.stream(ElementEvent.values()).map(Objects::toString).collect(Collectors.toList());
    }

    public EnemyLite createEnemy(@NotNull EnemyCreateRequest enemyCreateRequest) {
        var dto = enemyMapper.toDto(enemyCreateRequest);
        checkIfEnemyExists(dto.getName());
        var skill = skillRepository.get(dto.getSkillId());
        var enemy = enemyRepository.save(Enemy.of(dto, skill));
        return enemyMapper.toLite(enemy);
    }

    private void checkIfEnemyExists(@NotEmpty String enemyName) {
        if (enemyRepository.existsByName(enemyName)) {
            throw new EnemyAlreadyExistsException();
        }
    }
}

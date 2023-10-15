package com.michael1099.rest_rpg.enemy;

import lombok.RequiredArgsConstructor;
import org.openapitools.api.EnemyApi;
import org.openapitools.model.EnemyCreateRequest;
import org.openapitools.model.EnemyLite;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class EnemyController implements EnemyApi {

    private final EnemyService enemyService;

    @Override
    public ResponseEntity<EnemyLite> createEnemy(EnemyCreateRequest enemyCreateRequest) {
        return ResponseEntity.ok(enemyService.createEnemy(enemyCreateRequest));
    }

    @Override
    public ResponseEntity<List<String>> getStrategyActionsEnum() {
        return ResponseEntity.ok(enemyService.getStrategyActionsEnum());
    }

    @Override
    public ResponseEntity<List<String>> getStrategyEventsEnum() {
        return ResponseEntity.ok(enemyService.getStrategyEventsEnum());
    }
}

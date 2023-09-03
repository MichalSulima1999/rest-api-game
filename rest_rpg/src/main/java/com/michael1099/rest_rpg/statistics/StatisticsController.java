package com.michael1099.rest_rpg.statistics;

import lombok.RequiredArgsConstructor;
import org.openapitools.api.StatisticsApi;
import org.openapitools.model.StatisticsDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StatisticsController implements StatisticsApi {

    private final StatisticsService statisticsService;

    @Override
    public ResponseEntity<StatisticsDetails> getStatistics(Long characterId) {
        return ResponseEntity.ok(statisticsService.getStatistics(characterId));
    }
}

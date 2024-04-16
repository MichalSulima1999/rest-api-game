package com.michael1099.rest_rpg.enemy;

import jakarta.validation.constraints.NotNull;
import org.openapitools.model.EnemyCreateRequest;
import org.openapitools.model.EnemyLite;
import org.openapitools.model.EnemyLites;

// Tydzień 8 - Zastosuj zasadę Odwracania zależności
// Stworzone zostały 3 interfejsy: EnemyService, ItemService, ReportService,
// 3 klasy abstrakcyjne: AbstractEnemyService, AbstractItemService, AbstractReportService,
// implementacje: EnemyServiceImplementation, ItemServiceImplementation, ReportServiceImplementation.
// Przykład użycia w kontrolerze EnemyController: private final EnemyService enemyService; - używamy abstrakcji, a nie implementacji
public interface EnemyService {

    EnemyLite createEnemy(@NotNull EnemyCreateRequest enemyCreateRequest);

    EnemyLites getEnemies();
}
// Koniec Tydzień 8 - Zastosuj zasadę Odwracania zależności

package com.michael1099.rest_rpg.adventure;

import lombok.RequiredArgsConstructor;
import org.openapitools.api.AdventureApi;
import org.openapitools.model.AdventureBasicPage;
import org.openapitools.model.AdventureCreateRequest;
import org.openapitools.model.AdventureLite;
import org.openapitools.model.AdventureSearchRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class AdventureController implements AdventureApi {

    private final AdventureService adventureService;

    @Override
    public ResponseEntity<AdventureLite> createAdventure(AdventureCreateRequest adventureCreateRequest) {
        return ResponseEntity.ok(adventureService.createAdventure(adventureCreateRequest));
    }

    @Override
    public ResponseEntity<AdventureBasicPage> findAdventures(AdventureSearchRequest adventureSearchRequest) {
        return ResponseEntity.ok(adventureService.findAdventures(adventureSearchRequest));
    }
}

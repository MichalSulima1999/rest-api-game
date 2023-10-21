package com.michael1099.rest_rpg.skill;

import lombok.RequiredArgsConstructor;
import org.openapitools.api.SkillApi;
import org.openapitools.model.SkillCreateRequest;
import org.openapitools.model.SkillLite;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class SkillController implements SkillApi {

    private final SkillService skillService;

    @Override
    public ResponseEntity<SkillLite> createSkill(SkillCreateRequest skillCreateRequest) {
        return ResponseEntity.ok(skillService.createSkill(skillCreateRequest));
    }
}
